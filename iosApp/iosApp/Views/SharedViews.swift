import SwiftUI
import Shared

struct OfflineBanner: View {
    let visible: Bool

    var body: some View {
        if visible {
            HStack(spacing: 8) {
                Image(systemName: "wifi.slash")
                Text("You're offline. Showing saved recipes.")
                    .font(.subheadline)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(12)
            .background(Color.orange.opacity(0.18))
        }
    }
}

struct RecipeRow: View {
    let recipe: RecipeSummary

    var body: some View {
        HStack(spacing: 12) {
            RecipeThumb(url: recipe.thumbnailUrl, size: 64)
            VStack(alignment: .leading, spacing: 4) {
                Text(recipe.name)
                    .font(.headline)
                    .lineLimit(2)
                let meta = [recipe.category, recipe.area].compactMap { $0 }.filter { !$0.isEmpty }.joined(separator: " · ")
                if !meta.isEmpty {
                    Text(meta)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
            }
            Spacer()
            if recipe.isFavorite {
                Image(systemName: "heart.fill")
                    .foregroundStyle(.pink)
            }
        }
        .padding(.vertical, 4)
    }
}

struct RecipeGridCard: View {
    let recipe: RecipeSummary

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            RecipeThumb(url: recipe.thumbnailUrl, size: nil)
                .frame(minHeight: 120)
                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
            Text(recipe.name)
                .font(.headline)
                .lineLimit(2)
            let meta = [recipe.category, recipe.area].compactMap { $0 }.filter { !$0.isEmpty }.joined(separator: " · ")
            if !meta.isEmpty {
                Text(meta)
                    .font(.caption)
                    .foregroundStyle(.secondary)
                    .lineLimit(1)
            }
        }
    }
}

struct RecipeThumb: View {
    let url: String?
    let size: CGFloat?

    var body: some View {
        Group {
            if let url, let imageUrl = URL(string: url) {
                AsyncImage(url: imageUrl) { phase in
                    switch phase {
                    case .success(let image):
                        image.resizable().scaledToFill()
                    case .failure:
                        placeholder
                    case .empty:
                        ProgressView()
                    @unknown default:
                        placeholder
                    }
                }
            } else {
                placeholder
            }
        }
        .frame(width: size, height: size)
        .clipped()
        .background(Color(.secondarySystemBackground))
    }

    private var placeholder: some View {
        Image(systemName: "fork.knife")
            .font(.title)
            .foregroundStyle(.secondary)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
