import SwiftUI
import Shared

struct FavoritesView: View {
    @StateObject private var wrapper = FavoritesViewModelWrapper()

    private let columns = [
        GridItem(.adaptive(minimum: 160), spacing: 16),
    ]

    var body: some View {
        NavigationStack {
            Group {
                if wrapper.state.isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if recipes.isEmpty {
                    ContentUnavailableView(
                        "No favorites yet",
                        systemImage: "heart",
                        description: Text("Save recipes with the heart icon. They stay available offline.")
                    )
                } else {
                    ScrollView {
                        LazyVGrid(columns: columns, spacing: 16) {
                            ForEach(recipes, id: \.id) { recipe in
                                NavigationLink(value: recipe.id) {
                                    RecipeGridCard(recipe: recipe)
                                }
                                .buttonStyle(.plain)
                            }
                        }
                        .padding()
                    }
                }
            }
            .navigationTitle("Favorites")
            .navigationBarTitleDisplayMode(.large)
            .navigationDestination(for: String.self) { id in
                RecipeDetailView(recipeId: id)
            }
        }
    }

    private var recipes: [RecipeSummary] {
        KotlinLists.recipes(wrapper.state.recipes)
    }
}
