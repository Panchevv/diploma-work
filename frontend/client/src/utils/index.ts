import { computed, isRef } from "vue"

import { type ComputedRef, type Ref } from "vue"
import { MeasurementType, type Device } from "@/generated/graphql"
import type { DisplayableDevice } from "@/graphql/types"
import type { AnyVariables, UseQueryArgs, UseQueryResponse } from "@urql/vue"

/* CSS Helpers */
type ClassConditionalObject = {
    // @ts-expect-error
    [key: string | Array<string>]: boolean
}
export type UseQueryType = <T = any, V extends AnyVariables = AnyVariables>(args: UseQueryArgs<T, V>) => UseQueryResponse<T, V>

export type ClassObject = Array<string | ClassConditionalObject> | ClassConditionalObject
const isClassObject = (value: string | ClassObject): value is ClassObject => typeof value === "object"
const isClassConditionalObject = (value: string | ClassObject): value is ClassConditionalObject => typeof value === "object" && !Array.isArray(value)

const mapClassNameString = (className: string, styleObject: Record<string, string>): Array<string> =>
    className.split(/\s+/).map((classname: string) => classname in styleObject ? styleObject[classname] : classname)
const mapClassNameObject = (className: ClassConditionalObject, styleObject: Record<string, string>): ClassConditionalObject =>
    Object.keys(className).reduce((accumulator: ClassConditionalObject, currentValue: string) => {
        if (currentValue in styleObject)
            accumulator[styleObject[currentValue]] = className[currentValue]
        else
            accumulator[currentValue] = className[currentValue]
        return accumulator
    }, {})

/**
 * Picks CSS classes that exist in `styleObject` and returns the rest as is
 * Example: pickCssClass("bg-red circle form-heading btn", $style) => "bg-red _circle_asdf3_2 _form-heading_hdtg2_5 btn"
 * @returns A VueJS class object with classes picked from `styleObject` if they exist in `styleObject` and classes as provided if they don't
 */
export const pickCssClass = (className: string | ClassObject, styleObject: Record<string, string>): ClassObject => {
    if (!isClassObject(className))
        return mapClassNameString(className, styleObject)

    if (isClassConditionalObject(className))
        return mapClassNameObject(className, styleObject)

    // Array<string | ClassConditionalObject>
    const ret = className.reduce((accumulator: Array<string | ClassConditionalObject>, currentValue: string | ClassConditionalObject) => {
        if (typeof currentValue === "string")
            accumulator = accumulator.concat(mapClassNameString(currentValue, styleObject))
        else
            accumulator.push(mapClassNameObject(currentValue, styleObject))

        return accumulator
    }, [] as Array<string | ClassConditionalObject>)
    return ret
}

const extractClassNameString = (className: string, styleObject: Record<string, string>): Array<string> =>
    className.split(/\s+/).map((classname: string) => styleObject[classname])
const extractClassNameObject = (className: ClassConditionalObject, styleObject: Record<string, string>): ClassConditionalObject =>
    Object.keys(className).reduce((accumulator: ClassConditionalObject, currentValue: string) => {
        accumulator[styleObject[currentValue]] = className[currentValue]
        return accumulator
    }, {})
/**
 * Extracts CSS classes from `styleObject` so you don't have to type `$style.class1 $style.class2`. All passed classes must be in the `styleObject`
 * Example: pickCssClass("circle form-heading", $style) => "_circle_asdf3_2 _form-heading_hdtg2_5"
 * @returns A VueJS class object with classes picked from `styleObject`
 */
export const extractCssClass = (className: string | ClassObject, styleObject: Record<string, string>): ClassObject => {
    if (!isClassObject(className))
        return extractClassNameString(className, styleObject)

    if (isClassConditionalObject(className))
        return extractClassNameObject(className, styleObject)

    // Array<string | ClassConditionalObject>
    return className.reduce((accumulator: Array<string | ClassConditionalObject>, currentValue: string | ClassConditionalObject) => {
        if (typeof currentValue === "string")
            accumulator.concat(extractClassNameString(currentValue, styleObject))
        else
            accumulator.concat(extractClassNameObject(currentValue, styleObject))
        return accumulator
    }, [] as Array<string | ClassConditionalObject>)
}

export const computedWithPrevious = <T, >(callback: (previous: T | undefined) => T, clearPrevious?: boolean | Ref<boolean> | ComputedRef<boolean>) => {
    let previous: T | undefined = undefined
    return computed<T>(() => {
        if (clearPrevious !== undefined && isRef(clearPrevious) ? clearPrevious.value : clearPrevious)
            previous = undefined

        previous = callback(previous)
        return previous
    })
}

/** Typescript helpers **/
export const isDisplayableDevice = (device: Device): device is DisplayableDevice =>
    device.name != null

export function enumKeys<O extends object, K extends keyof O = keyof O>(obj: O): K[] {
    return Object.keys(obj).filter(k => Number.isNaN(+k)) as K[]
}
export const getObjectTypedKeys = Object.keys as <T extends object>(obj: T) => Array<keyof T>

/** Other **/

/**
 * Compares 2 Date objects without taking time into consideration
 **/
export const compareDates = (date1: Date | string, date2: Date | string): number => {
    const cDate1: Date = typeof date1 === "string" ? new Date(date1) : date1 as Date
    const cDate2: Date = typeof date2 === "string" ? new Date(date2) : date2 as Date

    const date1Date: Date = new Date(cDate1.getUTCFullYear(), cDate1.getUTCMonth(), cDate1.getUTCDate())
    const date2Date: Date = new Date(cDate2.getUTCFullYear(), cDate2.getUTCMonth(), cDate2.getUTCDate())

    return date1Date.getTime() - date2Date.getTime()
}

export type TimeAmount = {
    years?: number
    months?: number
    days?: number
    hours?: number
    minutes?: number
    seconds?: number
    milliseconds?: number
}
export const addToDate = (date: Date, amount: TimeAmount) => {
    const returnDate = new Date(date)
    if (amount.years !== undefined)
        returnDate.setFullYear(returnDate.getFullYear() + amount.years)
    if (amount.months !== undefined)
        returnDate.setMonth(returnDate.getMonth() + amount.months)
    if (amount.days !== undefined)
        returnDate.setDate(returnDate.getDate() + amount.days)
    if (amount.hours !== undefined)
        returnDate.setHours(returnDate.getHours() + amount.hours)
    if (amount.minutes !== undefined)
        returnDate.setMinutes(returnDate.getMinutes() + amount.minutes)
    if (amount.seconds !== undefined)
        returnDate.setSeconds(returnDate.getSeconds() + amount.seconds)
    if (amount.milliseconds !== undefined)
        returnDate.setMilliseconds(returnDate.getMilliseconds() + amount.milliseconds)

    return returnDate
}
export const subtractFromDate = (date: Date, amount: TimeAmount) => {
    const returnDate = new Date(date)
    if (amount.years !== undefined)
        returnDate.setFullYear(returnDate.getFullYear() - amount.years)
    if (amount.months !== undefined)
        returnDate.setMonth(returnDate.getMonth() - amount.months)
    if (amount.days !== undefined)
        returnDate.setDate(returnDate.getDate() - amount.days)
    if (amount.hours !== undefined)
        returnDate.setHours(returnDate.getHours() - amount.hours)
    if (amount.minutes !== undefined)
        returnDate.setMinutes(returnDate.getMinutes() - amount.minutes)
    if (amount.seconds !== undefined)
        returnDate.setSeconds(returnDate.getSeconds() - amount.seconds)
    if (amount.milliseconds !== undefined)
        returnDate.setMilliseconds(returnDate.getMilliseconds() - amount.milliseconds)

    return returnDate
}

export const units: Record<MeasurementType, string> = {
    [MeasurementType.AIR_PRESSURE]: "kPa",
    [MeasurementType.BATTERY_VOLTAGE]: "V",
    [MeasurementType.HUMIDITY]: "%",
    [MeasurementType.RSRP]: "dBm",
    [MeasurementType.TEMPERATURE]: "°C",
}