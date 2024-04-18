# FROM python:3.12.3-slim-bullseye

# ENV PYTHONDONTWRITEBYTECODE=1
# ENV PYTHONUNBUFFERED=1

# RUN python -m pip install -U pip && python -m pip install --no-cache-dir -r requirements.txt

# WORKDIR /app

# COPY . .

# EXPOSE 8000

# CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]


FROM python:3.8.10-slim

WORKDIR /app

COPY . /app

RUN pip install -r requirements.txt