import Foundation
import Shared

@MainActor
final class HomeViewModelWrapper: ObservableObject {
    @Published var state: HomeUiState
    private let viewModel: HomeViewModel
    private var watcher: FlowWatcher?

    init(viewModel: HomeViewModel = IosViewModelFactory().homeViewModel()) {
        self.viewModel = viewModel
        self.state = viewModel.currentState()
        watcher = IosFlowWatchKt.watchHomeState(viewModel: viewModel) { [weak self] newState in
            Task { @MainActor in
                self?.state = newState
            }
        }
    }

    func refresh() {
        viewModel.refresh()
    }

    func stop() {
        watcher?.close()
        watcher = nil
    }
}

@MainActor
final class SearchViewModelWrapper: ObservableObject {
    @Published var state: SearchUiState
    private let viewModel: SearchViewModel
    private var watcher: FlowWatcher?

    init(viewModel: SearchViewModel = IosViewModelFactory().searchViewModel()) {
        self.viewModel = viewModel
        self.state = viewModel.currentState()
        watcher = IosFlowWatchKt.watchSearchState(viewModel: viewModel) { [weak self] newState in
            Task { @MainActor in
                self?.state = newState
            }
        }
    }

    func onQueryChange(_ query: String) {
        viewModel.onQueryChange(value: query)
    }

    func stop() {
        watcher?.close()
        watcher = nil
    }
}

@MainActor
final class RecipeDetailViewModelWrapper: ObservableObject {
    @Published var state: RecipeDetailUiState
    private let viewModel: RecipeDetailViewModel
    private var watcher: FlowWatcher?

    init(recipeId: String) {
        let viewModel = IosViewModelFactory().recipeDetailViewModel(id: recipeId)
        self.viewModel = viewModel
        self.state = viewModel.currentState()
        watcher = IosFlowWatchKt.watchDetailState(viewModel: viewModel) { [weak self] newState in
            Task { @MainActor in
                self?.state = newState
            }
        }
    }

    func toggleFavorite() {
        viewModel.toggleFavorite()
    }

    func stop() {
        watcher?.close()
        watcher = nil
    }
}

@MainActor
final class FavoritesViewModelWrapper: ObservableObject {
    @Published var state: FavoritesUiState
    private let viewModel: FavoritesViewModel
    private var watcher: FlowWatcher?

    init(viewModel: FavoritesViewModel = IosViewModelFactory().favoritesViewModel()) {
        self.viewModel = viewModel
        self.state = viewModel.currentState()
        watcher = IosFlowWatchKt.watchFavoritesState(viewModel: viewModel) { [weak self] newState in
            Task { @MainActor in
                self?.state = newState
            }
        }
    }

    func stop() {
        watcher?.close()
        watcher = nil
    }
}

enum KotlinLists {
    static func recipes(_ list: Any?) -> [RecipeSummary] {
        if let recipes = list as? [RecipeSummary] {
            return recipes
        }
        if let nsArray = list as? NSArray {
            return nsArray.compactMap { $0 as? RecipeSummary }
        }
        return []
    }

    static func ingredients(_ list: Any?) -> [Ingredient] {
        if let ingredients = list as? [Ingredient] {
            return ingredients
        }
        if let nsArray = list as? NSArray {
            return nsArray.compactMap { $0 as? Ingredient }
        }
        return []
    }
}
