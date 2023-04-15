# FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build-env
# WORKDIR /home

# # Copy csproj and restore as distinct layers
# # COPY *.csproj ./
# # RUN dotnet restore

# # Copy everything else and build
# COPY ./requirements.txt /code/requirements.txt
# RUN dotnet publish -c Release -o out 

# # Build runtime image
# FROM mcr.microsoft.com/dotnet/aspnet:5.0
# WORKDIR ./app /code/app
# COPY --from=build-env /app/out .
# ENTRYPOINT ["dotnet", "aspnetapp.dll"]
# https://hub.docker.com/_/microsoft-dotnet

FROM mcr.microsoft.com/dotnet/aspnet:5.0 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["aspnetapp.csproj", ""]
RUN dotnet restore "./aspnetapp.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "aspnetapp.csproj" -c Release -o /app/build

RUN wget -q https://packages.microsoft.com/config/ubuntu/20.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb \
    && dpkg -i packages-microsoft-prod.deb \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        dotnet-sdk-5.0

FROM build AS publish
RUN dotnet publish "aspnetapp.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "aspnetapp.dll"]