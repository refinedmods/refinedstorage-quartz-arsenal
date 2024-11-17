plugins {
    id("refinedarchitect.root")
    id("refinedarchitect.base")
}

refinedarchitect {
    sonarQube("refinedmods_refinedstorage-quartz-arsenal", "refinedmods")
}

subprojects {
    group = "com.refinedmods.refinedstorage.quartzarsenal"
}
