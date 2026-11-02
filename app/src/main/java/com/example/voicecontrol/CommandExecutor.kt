package com.example.voicecontrol

/**
 * Turns a text command into an action. Later, the speech-to-text output goes straight in here,
 * and unknown commands can be forwarded to an LLM that returns a structured action.
 */
object CommandExecutor {

    fun execute(raw: String): String {
        val svc = VoiceAccessibilityService.instance
            ?: return "Accessibility service is not enabled"

        val text = raw.trim()
        val lower = text.lowercase()

        return when {
            lower == "back" || lower == "go back" -> report(svc.back(), "back")
            lower == "home" || lower == "go home" -> report(svc.home(), "home")
            lower == "recents" || lower == "recent apps" -> report(svc.recents(), "recents")
            lower == "notifications" -> report(svc.notifications(), "notifications")
            lower == "scroll down" -> report(svc.scroll(true), "scroll down")
            lower == "scroll up" -> report(svc.scroll(false), "scroll up")

            lower.startsWith("open ") -> {
                val app = text.substring(5).trim()
                report(svc.openApp(app), "open $app")
            }
            lower.startsWith("tap ") -> {
                val label = text.substring(4).trim()
                report(svc.tapByText(label), "tap $label")
            }
            lower.startsWith("click ") -> {
                val label = text.substring(6).trim()
                report(svc.tapByText(label), "tap $label")
            }
            lower.startsWith("type ") -> {
                val content = text.substring(5)
                report(svc.typeText(content), "type \"$content\"")
            }
            else -> "Unknown command: $text"
        }
    }

    private fun report(ok: Boolean, what: String) = if (ok) "Done: $what" else "Failed: $what"
}
