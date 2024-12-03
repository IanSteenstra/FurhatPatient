package furhatos.app.openaichat.flow

import furhatos.app.openaichat.flow.chatbot.MainChat
import furhatos.app.openaichat.flow.chatbot.serviceKey
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

val Init: State = state {
    init {
        /** Check API key for  OpenAI has been set */
        if (serviceKey.isEmpty()) {
            println("Missing API key for OpenAI language model. ")
            exit()
        }

        /** start the interaction */
        goto(MainChat)
    }
}