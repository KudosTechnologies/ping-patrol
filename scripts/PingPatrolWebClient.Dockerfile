FROM node:20.18-slim as react_builder

COPY kudconnect-web-client kudconnect-web-client
WORKDIR kudconnect-web-client
RUN npm install

RUN npm run build:dev

FROM nginx:alpine

COPY --from=react_builder kudconnect-web-client/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]