import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var wrapper = HomeViewModelWrapper()

    private let columns = [
        GridItem(.adaptive(minimum: 160), spacing: 16),
    ]

    var body: some View {
        NavigationStack {
            Group {
                if wrapper.state.isLoading && recipes.isEmpty {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                else if recipes.isEmpty {
                    ContentUnavailableView(
                        "No recipes yet",
                        systemImage: "fork.knife",
                        description: Text("Pull to refresh when you're online.")
                    )
                }
                else {
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
            .safeAreaInset(edge: .top) {
                OfflineBanner(visible: wrapper.state.isOffline)
            }
            .navigationTitle("Home")
            .navigationBarTitleDisplayMode(.large)
            .refreshable {
                wrapper.refresh()
            }
            .navigationDestination(for: String.self) { id in
                RecipeDetailView(recipeId: id)
            }
        }
    }

    private var recipes: [RecipeSummary] {
        KotlinLists.recipes(wrapper.state.recipes)
    }
}
