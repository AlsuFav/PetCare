pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PetCare"
include(":app")
include(":core:network")
include(":core:navigation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:util")
include(":feature:registration")
include(":feature:home")
include(":feature:authorization")
include(":feature:splash")
include(":feature:profile")
include(":feature:safety")
include(":feature:pet")
include(":feature:pet:all")
include(":feature:pet:details")
include(":feature:pet:add")
include(":feature:appointment:all")
include(":feature:appointment:add")
include(":feature:appointment:details")
include(":feature:appointment:add:pet")
include(":feature:appointment:add:service")
include(":feature:appointment:add:timeslot")
include(":feature:appointment:add:confirm")
include(":core:database")
include(":feature:service")
