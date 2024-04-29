import { type ObjectDirective } from "vue"

export const vShowOldKey = Symbol("_vod")

interface VShowElement extends HTMLElement {
  // _vod = vue original display
  [vShowOldKey]: string
}

export const vShow: ObjectDirective<VShowElement> = {
    beforeMount(el, { value, modifiers }, { transition }) {
        el[vShowOldKey] = el.style.display === "none" ? "" : el.style.display
        if (transition && value) {
            transition.beforeEnter(el)
        } else {
            setDisplay(el, value, modifiers.important)
        }
    },
    mounted(el, { value }, { transition }) {
        if (transition && value) {
            transition.enter(el)
        }
    },
    updated(el, { value, oldValue, modifiers }, { transition }) {
        if (!value === !oldValue) return
        if (transition) {
            if (value) {
                transition.beforeEnter(el)
                setDisplay(el, true, modifiers.important)
                transition.enter(el)
            } else {
                transition.leave(el, () => {
                    setDisplay(el, false, modifiers.important)
                })
            }
        } else {
            setDisplay(el, value, modifiers.important)
        }
    },
    beforeUnmount(el, { value, modifiers }) {
        setDisplay(el, value, modifiers.important)
    },
}

function setDisplay(el: VShowElement, value: unknown, important: boolean = false): void {
    el.style.setProperty("display", value ? el[vShowOldKey] : "none", important ? "important" : undefined)
}

// SSR vnode transforms, only used when user includes client-oriented render
// function in SSR
// 'important' modifier doesn't work in SSR
export function initVShowForSSR() {
    vShow.getSSRProps = ({ value }) => {
        if (!value) {
            return { style: { display: "none" } }
        }
    }
}
