plugins {
    id("com.refinedmods.refinedarchitect.fabric")
}

repositories {
    maven {
        name = "Refined Storage"
        url = uri("https://maven.creeperhost.net")
        content {
            includeGroup("com.refinedmods.refinedstorage")
        }
    }
    maven {
        name = "ModMenu"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        name = "Cloth Config"
        url = uri("https://maven.shedaniel.me/")
    }
}

refinedarchitect {
    modId = "refinedstorage_quartz_arsenal"
    fabric()
    publishing {
        maven = true
        curseForge = "1230483"
        curseForgeRequiredDependencies = listOf("fabric-api", "refined-storage")
        modrinth = "gnwGOmBf"
        modrinthRequiredDependencies = listOf("fabric-api", "refined-storage")
    }
}

base {
    archivesName.set("refinedstorage-quartz-arsenal-fabric")
}

val refinedstorageVersion: String by project

val commonJava by configurations.existing
val commonResources by configurations.existing

dependencies {
    compileOnly(project(":refinedstorage-quartz-arsenal-common"))
    commonJava(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonJava"))
    commonResources(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonResources"))
    modApi("com.refinedmods.refinedstorage:refinedstorage-fabric:${refinedstorageVersion}")
}
