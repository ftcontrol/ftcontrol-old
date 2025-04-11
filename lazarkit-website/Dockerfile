FROM oven/bun:alpine AS build

WORKDIR /app

RUN apk add --no-cache git

COPY ./lazarkit-website/bun.lock ./lazarkit-website/package.json ./
RUN bun install

COPY ./lazarkit-website/ ./

RUN bun run build

FROM busybox:latest AS runtime
COPY --from=build /app/dist /www
EXPOSE 80
CMD ["httpd", "-f", "-v", "-p", "80", "-h", "/www"]
