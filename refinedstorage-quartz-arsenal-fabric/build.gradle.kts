plugins {
    id("refinedarchitect.fabric")
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/refinedmods/refinedstorage2")
        credentials {
            username = "anything"
            password = "\u0067hp_oGjcDFCn8jeTzIj4Ke9pLoEVtpnZMP4VQgaX"
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
