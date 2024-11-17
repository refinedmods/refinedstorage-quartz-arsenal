plugins {
    id("refinedarchitect.neoforge")
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/refinedmods/refinedstorage2")
        credentials {
            username = "anything"
            password = "\u0067hp_oGjcDFCn8jeTzIj4Ke9pLoEVtpnZMP4VQgaX"
        }
    }
}

refinedarchitect {
    modId = "refinedstorage_quartz_arsenal"
    neoForge()
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
