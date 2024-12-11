package furhatos.app.openaichat.flow

import furhatos.flow.kotlin.*
import furhatos.gestures.ARKitParams
import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import kotlin.random.Random

// list of all Furhat parameters https://docs.furhat.io/facecore/

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

fun MyExpressHappy() = defineGesture("MyExpressHappy") {
    frame(0.3, 3.0) {
            BasicParams.SMILE_OPEN to 0.8
            ARKitParams.BROW_OUTER_UP_LEFT to 1
            ARKitParams.BROW_OUTER_UP_RIGHT to 1
            ARKitParams.CHEEK_PUFF to 0.8

        }
        reset(3.0)
    }

fun MyExpressSad() = defineGesture("MyExpressSad") {
    frame(0.3, 3.0) {
        BasicParams.EXPR_SAD to 0.3
        ARKitParams.EYE_SQUINT_LEFT to 0.2
        ARKitParams.EYE_SQUINT_RIGHT to 0.2
        ARKitParams.BROW_DOWN_RIGHT to 0.2
        ARKitParams.BROW_DOWN_LEFT to 0.2
        ARKitParams.BROW_INNER_UP to 0.5
    }
    reset(3.0)
}

fun MyExpressSurprise() = defineGesture("MyExpressSurprise") {
    frame(0.3, 3.0) {
        BasicParams.SURPRISE to 0.3
        //BasicParams.PHONE_OH to 0.3
        ARKitParams.JAW_OPEN to 0.9
        ARKitParams.BROW_INNER_UP to 1
        ARKitParams.BROW_OUTER_UP_RIGHT to 0.5
        ARKitParams.BROW_OUTER_UP_LEFT to 0.5

    }
    reset(3.0)
}

fun MyExpressDisapproval() = defineGesture("MyExpressDisapproval") {
    frame(0.3, 2.0) {
        ARKitParams.BROW_DOWN_LEFT to 0.2
        ARKitParams.BROW_DOWN_RIGHT to 0.2
        BasicParams.EXPR_DISGUST to 0.3
        ARKitParams.MOUTH_DIMPLE_LEFT to 1
        ARKitParams.MOUTH_DIMPLE_RIGHT to 1
        ARKitParams.EYE_SQUINT_LEFT to 0.3
        ARKitParams.EYE_SQUINT_RIGHT to 0.3
    }
    frame(1.0, 2.5) {
        BasicParams.BROW_IN_LEFT to 0.3
        BasicParams.BROW_IN_RIGHT to 0.3
    }
    reset(2.7)
}

fun MyExpressFear() = defineGesture("MyExpressFear") {
    frame(0.3, 2.0) {
        BasicParams.EXPR_FEAR to 0.8
        BasicParams.BROW_UP_LEFT to 0.8
        BasicParams.BROW_UP_RIGHT to 0.8
        BasicParams.PHONE_AAH to 0.3
    }
    frame(1.0, 2.0) {
        BasicParams.GAZE_TILT to 20
    }
    reset(2.5)
}

fun MyExpressAnger() = defineGesture("MyExpressAnger") {
    frame(0.3, 2.0) {
        BasicParams.EXPR_ANGER to 0.5
        BasicParams.BROW_IN_LEFT to 0.7
        BasicParams.BROW_IN_RIGHT to 0.7
        BasicParams.BROW_DOWN_LEFT to 0.5
        BasicParams.BROW_DOWN_RIGHT to 0.5
    }
    frame(1.0, 2.0) {
        BasicParams.PHONE_F_V to 0.5
    }

    reset(2.5)
}

fun MyExpressDisgust() = defineGesture("MyExpressDisgust") {
    frame(0.3, 1.5) {
        BasicParams.EXPR_DISGUST to 0.7
        BasicParams.BROW_IN_LEFT to 0.5
        BasicParams.BROW_IN_RIGHT to 0.5
        ARKitParams.BROW_DOWN_LEFT to 0.5
        ARKitParams.BROW_DOWN_RIGHT to 0.5
        ARKitParams.EYE_SQUINT_LEFT to 0.7
        ARKitParams.EYE_SQUINT_RIGHT to 0.7
    }
    reset(2.0)
}

fun MyExpressRelief() = defineGesture("MyExpressRelief") {
    frame(0.3, 1.0) {
        BasicParams.SMILE_CLOSED to 1
        BasicParams.LOOK_UP_LEFT to 0.8
        BasicParams.LOOK_UP_LEFT to 0.8
    }

    frame(1.0, 1.0) {
        BasicParams.BLINK_LEFT to 1
        BasicParams.BLINK_RIGHT to 1
    }

    frame(1.5, 1.7){
        BasicParams.PHONE_OOH_Q
    }

    reset(2.0)
}
