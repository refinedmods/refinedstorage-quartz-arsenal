plugins {
    id("com.refinedmods.refinedarchitect.neoforge")
}

repositories {
    maven {
        name = "Refined Storage"
        url = uri("https://maven.creeperhost.net")
        content {
            includeGroup("com.refinedmods.refinedstorage")
        }
    }
}

refinedarchitect {
    modId = "refinedstorage_quartz_arsenal"
    neoForge()
    publishing {
        maven = true
        curseForge = "1230483"
        curseForgeRequiredDependencies = listOf("refined-storage")
        modrinth = "gnwGOmBf"
        modrinthRequiredDependencies = listOf("refined-storage")
    }
}

base {
    archivesName.set("refinedstorage-quartz-arsenal-neoforge")
}

val refinedstorageVersion: String by project

val commonJava by configurations.existing
val commonResources by configurations.existing

dependencies {
    compileOnly(project(":refinedstorage-quartz-arsenal-common"))
    commonJava(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonJava"))
    commonResources(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonResources"))
    api("com.refinedmods.refinedstorage:refinedstorage-neoforge:${refinedstorageVersion}")
}
