# Stage 1: Build Frontend
FROM node:18 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Backend + Frontend together
FROM node:18
WORKDIR /app
COPY backend/package*.json ./backend/
WORKDIR /app/backend
RUN npm install
COPY backend/ ./
# Copy built frontend to backend's public folder
COPY --from=frontend-build /app/frontend/dist ./public

EXPOSE 10000
CMD ["node", "server.js"]
