package furhatos.app.demo.flow

import furhatos.flow.kotlin.*
import furhatos.app.demo.nlg.ChatGPT
import kotlinx.coroutines.*

import java.lang.System.getenv

val apiKey = getenv("OPENAI_API_KEY") ?: throw IllegalStateException("API Key not found")
val chatGPT = ChatGPT(apiKey) // Initialize with the API key from env variables

val init: State = state {

    init {
        furhat.say("Hello! How can I assist you today?")
        goto(WaitForUser)
    }
 
    onUserLeave(instant = true) {
        furhat.say("Goodbye!")
    }
}

val WaitForUser: State = state {
    onEntry {
        furhat.listen()
    }

    onReentry {
        furhat.listen()
    }

    onResponse {
        val message = it.text
        furhat.say("Let me think...")

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    chatGPT.chat(message)
                }
                // Simply execute the line below if switching context is unnecessary
                furhat.say(response)
            } catch (e: Exception) {
                e.printStackTrace()
                furhat.say("An error occurred. Please try again.")
            }
        }
        goto(init)
    }
}