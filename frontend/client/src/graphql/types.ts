import type { Device } from "@/generated/graphql"

/** Custom or modified types **/
export type DisplayableDevice = Device & {
    name: string
}

export type SearchItem = {
    value: number | string
    title: string
}