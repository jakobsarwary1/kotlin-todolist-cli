import java.io.File

data class Task(var text: String, var done: Boolean = false)

val file = File("todo.txt")
val tasks = mutableListOf<Task>()

fun loadTasks() {
    if (file.exists()) {
        file.readLines().forEach {
            val parts = it.split(";;")
            if (parts.size == 2) {
                tasks.add(Task(parts[1], parts[0] == "done"))
            }
        }
    }
}

fun saveTasks() {
    file.writeText(tasks.joinToString("\n") {
        val status = if (it.done) "done" else "open"
        "$status;;${it.text}"
    })
}

fun showTasks() {
    if (tasks.isEmpty()) println("Keine Aufgaben.") else {
        println("Aufgabenliste:")
        tasks.forEachIndexed { i, task ->
            val check = if (task.done) "[x]" else "[ ]"
            println("${i + 1}. $check ${task.text}")
        }
    }
}

fun main() {
    loadTasks()
    while (true) {
        println("\n--- ToDo CLI ---")
        println("1. Aufgabe hinzufügen")
        println("2. Aufgaben anzeigen")
        println("3. Als erledigt markieren")
        println("4. Aufgabe löschen")
        println("5. Beenden")
        print("Wähle: ")
        when (readlnOrNull()?.trim()) {
            "1" -> {
                print("Neue Aufgabe: ")
                val text = readlnOrNull()?.trim() ?: ""
                if (text.isNotEmpty()) tasks.add(Task(text))
                saveTasks()
            }
            "2" -> showTasks()
            "3" -> {
                showTasks()
                print("Nummer der Aufgabe: ")
                val index = readlnOrNull()?.toIntOrNull()?.minus(1)
                if (index != null && index in tasks.indices) tasks[index].done = true
                saveTasks()
            }
            "4" -> {
                showTasks()
                print("Nummer der Aufgabe: ")
                val index = readlnOrNull()?.toIntOrNull()?.minus(1)
                if (index != null && index in tasks.indices) tasks.removeAt(index)
                saveTasks()
            }
            "5" -> {
                println("Tschüss!")
                break
            }
            else -> println("Ungültige Eingabe.")
        }
    }
}
