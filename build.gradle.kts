// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
}

// 👇 注意：这里不要有任何 repositories { ... } 代码块！
// 所有的仓库配置都在 settings.gradle.kts 里完成了
// 保留这里是为了防止旧版 Gradle 报错，但如果上面的 settings 已配置，这里可以留空或仅保留插件