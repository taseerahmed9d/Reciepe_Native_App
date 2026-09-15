import SwiftUI
import Shared

struct SearchView: View {
    @StateObject private var wrapper = SearchViewModelWrapper()

    var body: some View {
        NavigationStack {
            Group {
                if wrapper.state.isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if wrapper.state.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    ContentUnavailableView(
                        "Search recipes",
                        systemImage: "magnifyingglass",
                        description: Text("Try pasta or chicken.")
                    )
                } else if results.isEmpty {
                    ContentUnavailableView.search
                } else {
                    List {
                        ForEach(results, id: \.id) { recipe in
                            NavigationLink(value: recipe.id) {
                                RecipeRow(recipe: recipe)
                            }
                        }
                    }
                    .listStyle(.plain)
                }
            }
            .safeAreaInset(edge: .top) {
                OfflineBanner(visible: wrapper.state.isOffline)
            }
            .navigationTitle("Search")
            .navigationBarTitleDisplayMode(.large)
            .searchable(text: Binding(
                get: { wrapper.state.query },
                set: { wrapper.onQueryChange($0) }
            ), prompt: "Search recipes")
            .navigationDestination(for: String.self) { id in
                RecipeDetailView(recipeId: id)
            }
        }
    }

    private var results: [RecipeSummary] {
        KotlinLists.recipes(wrapper.state.results)
    }
}
