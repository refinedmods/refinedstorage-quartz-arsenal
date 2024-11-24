plugins {
    id("refinedarchitect.common")
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
    common()
}

base {
    archivesName.set("refinedstorage-quartz-arsenal-common")
}

val refinedstorageVersion: String by project

dependencies {
    api("com.refinedmods.refinedstorage:refinedstorage-common:${refinedstorageVersion}")
}
