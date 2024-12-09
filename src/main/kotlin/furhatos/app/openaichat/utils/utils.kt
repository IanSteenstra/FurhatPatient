package furhatos.app.openaichat.flow

import furhatos.flow.kotlin.*
import furhatos.gestures.ARKitParams
import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import kotlin.random.Random


fun FlowControlRunner.askForAnything(text: String) {

    call(state {
        onEntry {
            furhat.ask(text)
        }
        onResponse {
            terminate()
        }
    })

}

val random = Random(0)

fun GazeAversion(duration: Double = 1.0) = defineGesture("GazeAversion") {
    val dur = duration.coerceAtLeast(0.2)
    frame(0.05, dur-0.05) {
        when (random.nextInt(4)) {
            0 -> {
                ARKitParams.EYE_LOOK_DOWN_LEFT to 0.5
                ARKitParams.EYE_LOOK_DOWN_RIGHT to 0.5
                ARKitParams.EYE_LOOK_OUT_LEFT to 0.5
                ARKitParams.EYE_LOOK_IN_RIGHT to 0.5
            }
            1 -> {
                ARKitParams.EYE_LOOK_UP_LEFT to 0.5
                ARKitParams.EYE_LOOK_UP_RIGHT to 0.5
                ARKitParams.EYE_LOOK_OUT_LEFT to 0.5
                ARKitParams.EYE_LOOK_IN_RIGHT to 0.5
            }
            2 -> {
                ARKitParams.EYE_LOOK_DOWN_LEFT to 0.5
                ARKitParams.EYE_LOOK_DOWN_RIGHT to 0.5
                ARKitParams.EYE_LOOK_OUT_RIGHT to 0.5
                ARKitParams.EYE_LOOK_IN_LEFT to 0.5
            }
            else -> {
                ARKitParams.EYE_LOOK_UP_LEFT to 0.5
                ARKitParams.EYE_LOOK_UP_RIGHT to 0.5
                ARKitParams.EYE_LOOK_OUT_RIGHT to 0.5
                ARKitParams.EYE_LOOK_IN_LEFT to 0.5
            }
        }
    }
    reset(dur)
}


val MyExpressHappy = defineGesture("MyExpressHappy") {
    frame(0.3, 3.0) {
        SMILE_OPEN to 0.8
        BROW_UP_LEFT to 1
        BROW_UP_RIGHT to 1
    }
    reset(3.0)
}

val MyExpressSad = defineGesture("MyExpressSad") {
    frame(0.3, 3.0) {
        EXPR_SAD to 0.3
        EYE_SQUINT_LEFT to 0.2
        EYE_SQUINT_RIGHT to 0.2
        BROW_DOWN_RIGHT to 0.2
        BROW_DOWN_LEFT to 0.2
    }
    reset(3.0)
}

val MyExpressSurprise = defineGesture("MyExpressSurprise") {
    frame(0.3, 3.0) {
        SURPRISE to 0.3
        PHONE_OH to 0.3
        BROW_UP_RIGHT to 1
        BROW_UP_LEFT to 1
    }
    reset(3.0)
}

val MyExpressDisapproval = defineGesture("MyExpressDisapproval") {
    frame(0.3, 2.0) {
        BROW_DOWN_LEFT to 0.2
        BROW_DOWN_RIGHT to 0.2
        EXPR_DISGUST to 0.3
        EYE_SQUINT_LEFT to 0.3
        EYE_SQUINT_RIGHT to 0.3
    }
    frame(1.0, 2.5) {
        BROW_IN_LEFT to 0.3
        BROW_IN_RIGHT to 0.3
    }
    reset(2.7)
}

val MyExpressFear = defineGesture("MyExpressFear") {
    frame(0.3, 2.0) {
        EXPR_FEAR to 0.8
        BROW_UP_LEFT to 0.8
        BROW_UP_RIGHT to 0.8
        PHONE_AAH to 0.3
    }
    frame(1.0, 2.0) {
        GAZE_TILT to 20
    }
    reset(2.5)
}

val MyExpressAnger = defineGesture("MyExpressAnger") {
    frame(0.3, 2.0) {
        EXPR_ANGER to 0.5
        BROW_IN_LEFT to 0.7
        BROW_IN_RIGHT to 0.7
        BROW_DOWN_LEFT to 0.5
        BROW_DOWN_RIGHT to 0.5
    }
    frame(1.0, 2.0) {
        PHONE_F_V to 0.5
    }

    reset(2.5)
}

val MyExpressDisgust = defineGesture("MyExpressDisgust") {
    frame(0.3, 1.5) {
        EXPR_DISGUST to 0.7
        BROW_IN_LEFT to 0.5
        BROW_IN_RIGHT to 0.5
        BROW_DOWN_LEFT to 0.5
        BROW_DOWN_RIGHT to 0.5
        EYE_SQUINT_LEFT to 0.7
        EYE_SQUINT_RIGHT to 0.7
    }
    reset(2.0)
}

val MyExpressRelief = defineGesture("MyExpressRelief") {
    frame(0.3, 1.0) {
        SMILE_CLOSED to 1
        LOOK_UP_LEFT to 0.8
        LOOK_UP_LEFT to 0.8
    }

    frame(1.0, 1.0) {
        BLINK_LEFT to 1
        BLINK_RIGHT to 1
    }

    frame(1.5, 1.7){
        PHONE_OOH_Q
    }

    reset(2.0)
}
