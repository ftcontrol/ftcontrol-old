FROM ubuntu:latest AS build

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    jq \
    tar \
    git \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Install Rust
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y --profile=minimal \
    && export PATH="$HOME/.cargo/bin:$PATH" \
    && rustup install stable \
    && rustup default stable

# Install mdBook
RUN export PATH="$HOME/.cargo/bin:$PATH" \
    && cargo install mdbook

COPY book.toml book.toml 
COPY src src

# Build the book
RUN . "$HOME/.cargo/env" && mdbook build

# Final runtime image with a lightweight HTTP server
FROM busybox:latest AS runtime
COPY --from=build /book .
EXPOSE 80
CMD ["busybox", "httpd", "-f", "-v", "-p", "80"]