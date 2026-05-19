FROM nginx:alpine

RUN echo "<h1>PasswordVault Kubernetes Deployment Successful </h1>" > /usr/share/nginx/html/index.html