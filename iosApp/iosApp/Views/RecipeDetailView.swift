import SwiftUI
import Shared

struct RecipeDetailView: View {
    @StateObject private var wrapper: RecipeDetailViewModelWrapper
    @State private var checkedIngredients: Set<String> = []

    init(recipeId: String) {
        _wrapper = StateObject(wrappedValue: RecipeDetailViewModelWrapper(recipeId: recipeId))
    }

    var body: some View {
        Group {
            if wrapper.state.isLoading && wrapper.state.recipe == nil {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            else if let recipe = wrapper.state.recipe {
                ScrollView {
                    VStack(alignment: .leading, spacing: 16) {
                        RecipeThumb(url: recipe.thumbnailUrl, size: nil)
                            .frame(height: 240)
                            .frame(maxWidth: .infinity)
                            .clipShape(RoundedRectangle(cornerRadius: 0))

                        VStack(alignment: .leading, spacing: 16) {
                            let meta = [recipe.category, recipe.area]
                                .compactMap { $0 }
                                .filter { !$0.isEmpty }
                                .joined(separator: " · ")
                            if !meta.isEmpty {
                                Text(meta)
                                    .font(.subheadline)
                                    .foregroundStyle(.secondary)
                            }

                            Text("Ingredients")
                                .font(.title2.bold())
                            ForEach(Array(KotlinLists.ingredients(recipe.ingredients).enumerated()), id: \.offset) { _, ingredient in
                                let key = "\(ingredient.measure)-\(ingredient.name)"
                                Button {
                                    if checkedIngredients.contains(key) {
                                        checkedIngredients.remove(key)
                                    } else {
                                        checkedIngredients.insert(key)
                                    }
                                } label: {
                                    HStack(alignment: .top) {
                                        Image(systemName: checkedIngredients.contains(key) ? "checkmark.circle.fill" : "circle")
                                            .foregroundStyle(checkedIngredients.contains(key) ? Color.accentColor : Color.secondary)
                                        Text([ingredient.measure, ingredient.name].filter { !$0.isEmpty }.joined(separator: " "))
                                            .foregroundStyle(.primary)
                                            .strikethrough(checkedIngredients.contains(key))
                                        Spacer()
                                    }
                                }
                                .buttonStyle(.plain)
                            }

                            Text("Instructions")
                                .font(.title2.bold())
                            Text(recipe.instructions ?? "")
                                .font(.body)
                        }
                        .padding(.horizontal)
                        .padding(.bottom, 24)
                    }
                }
            }
            else {
                ContentUnavailableView(
                    "Recipe unavailable",
                    systemImage: "exclamationmark.triangle",
                    description: Text(wrapper.state.error ?? "This recipe isn't cached yet.")
                )
            }
        }
        .safeAreaInset(edge: .top) {
            OfflineBanner(visible: wrapper.state.isOffline)
        }
        .navigationTitle(wrapper.state.recipe?.name ?? "Recipe")
        .navigationBarTitleDisplayMode(.inline)
        .onDisappear { wrapper.stop() }
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button {
                    wrapper.toggleFavorite()
                } label: {
                    Image(systemName: wrapper.state.recipe?.isFavorite == true ? "heart.fill" : "heart")
                }
                .disabled(wrapper.state.recipe == nil)
                .accessibilityLabel(
                    wrapper.state.recipe?.isFavorite == true ? "Remove from favorites" : "Add to favorites"
                )
            }
        }
    }
}
