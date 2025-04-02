# Chapter 1

This is just an example for a cover photo!

## Introduction

[mdBook](https://rust-lang.github.io/mdBook/#introduction) is a command-line tool for creating books using Markdown. It is ideal for creating product or API documentation, tutorials, course materials, or anything that requires a clean, easily navigable, and customizable presentation.

Built by the Rust community, mdBook is often compared to GitBook but is implemented in Rust for speed, safety, and simplicity. It offers a range of features, including:
- Lightweight Markdown syntax to help you focus more on content
- Integrated search support
- Color syntax highlighting for many programming languages
- Customizable themes to style the output
- Support for preprocessors to extend syntax and modify content
- Multiple backend options for exporting the book
- Automated testing of Rust code samples

While working on a library I was developing, I needed a reliable way to generate and serve its documentation. To streamline the process, I decided to Dockerize mdBook, making it easier to build, deploy, and maintain across different environments.

In this guide, we will create a Docker image that builds and serves an mdBook static site using a lightweight HTTP server.

---
## Prerequisites
Before proceeding, ensure you have the following installed:
1. [Docker](https://docs.docker.com/get-docker/)
2. [mdBook](https://rust-lang.github.io/mdBook/guide/installation.html) (for local testing)
	Executable binaries (Windows, macOS, or Linux) are available for download on the [GitHub Releases page](https://github.com/rust-lang/mdBook/releases) or by using cargo.
	`cargo install mdbook`

## Setting Up the Project

First, initialize an mdBook project:
```bash
mdbook init my-docs
cd my-docs
```
This creates a `src/` directory with sample content and a `book.toml` configuration file. You can edit `src/SUMMARY.md` to define the book's structure.

Command for testing your book locally:
```sh
mdbook serve --open
```

## Writing the Dockerfile

```docker
# Build Stage
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

# Copy project files
COPY book.toml book.toml
COPY src src

# Build the book
RUN . "$HOME/.cargo/env" && mdbook build

# Runtime Stage
FROM busybox:latest AS runtime

# Copy built book from build stage
COPY --from=build /book .

# Expose port for HTTP server
EXPOSE 80

# Start a lightweight HTTP server
CMD ["busybox", "httpd", "-f", "-v", "-p", "80"]
```

## Building and Running the Docker Container

### Build the Image

Navigate to the root of your `mdBook` project and build the Docker image:

```sh
docker build -t mdbook-site .
```

### Run the Container

To start the mdBook server inside the container:

```sh
docker run -p 8080:80 mdbook-site
```

Now, open `http://localhost:8080` in your browser to view the documentation.
## Conclusion

By Dockerizing your mdBook setup, you create a consistent and portable environment for serving documentation. This setup is ideal for CI/CD pipelines, cloud deployment, and local development. Happy documenting!

For more details about using mdBook, check out their [official documentation](https://rust-lang.github.io/mdBook).

Happy documenting! 🚀

