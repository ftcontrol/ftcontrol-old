FROM oven/bun:latest AS build

WORKDIR /app

RUN apt-get update && apt-get install -y git

COPY ./lazarkit-website/package.json ./lazarkit-website/package.json
COPY ./lazarkit-website/bun.lock ./lazarkit-website/bun.lock

COPY ./lazarkit-website ./lazarkit-website

RUN cd lazarkit-website && bun install

COPY ./test-codebase ./test-codebase

COPY ./library ./library

COPY ./ ./

RUN cd lazarkit-website && bun run build

# CMD ["sleep", "infinity"]

FROM busybox:latest AS runtime
COPY --from=build /app/lazarkit-website/dist /www
EXPOSE 80
CMD ["httpd", "-f", "-v", "-p", "80", "-h", "/www"]
