/*
 * Copyright [2023] [Jonathan Rivera Vasquez]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.project.utility.animation

import android.view.View

class Animating {

    fun animateViewsTranslationY(view: View, endY: Float, duration: Long, delay: Long) {
        view.animateTranslateY(endY, duration, delay)
    }

    fun animateViewsTranslationX(view: View, endX: Float, duration: Long, delay: Long) {
        view.animateTranslateX(endX, duration, delay)
    }

    private fun View.animateTranslateY(endY: Float, duration: Long, delay: Long) {
        animate().translationY(endY)
            .setDuration(duration)
            .setStartDelay(delay)
            .start()
    }

    private fun View.animateTranslateX(endX: Float, duration: Long, delay: Long) {
        animate().translationX(endX)
            .setDuration(duration)
            .setStartDelay(delay)
            .start()
    }
}