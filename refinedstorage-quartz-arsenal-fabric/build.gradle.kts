plugins {
    id("refinedarchitect.fabric")
}

refinedarchitect {
    modId = "refinedstorage_quartz_arsenal"
    fabric()
}

base {
    archivesName.set("refinedstorage-quartz-arsenal-fabric")
}

val commonJava by configurations.existing
val commonResources by configurations.existing

dependencies {
    compileOnly(project(":refinedstorage-quartz-arsenal-common"))
    commonJava(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonJava"))
    commonResources(project(path = ":refinedstorage-quartz-arsenal-common", configuration = "commonResources"))
}
