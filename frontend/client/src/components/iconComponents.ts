/**
 * Icon imports for SvgIcon component so they don't have to be imported in every file in order to be used
 * Can also be used outside SvgIcon component
 */

import AccountBoxFilled from "@material-design-icons/svg/filled/account_box.svg"
import AddCircleFilled from "@material-design-icons/svg/filled/add_circle.svg"
import ContactsFilled from "@material-design-icons/svg/filled/contacts.svg"
import EditFilled from "@material-design-icons/svg/filled/edit.svg"
import SensorsFilled from "@material-design-icons/svg/filled/sensors.svg"
import HistoryFilled from "@material-design-icons/svg/filled/history.svg"
import LogoutFilled from "@material-design-icons/svg/filled/logout.svg"
import PeopleFilled from "@material-design-icons/svg/filled/group_work.svg"
import AccountCircleRound from "@material-design-icons/svg/round/account_circle.svg"
import AddRound from "@material-design-icons/svg/round/add.svg"
import CloseRound from "@material-design-icons/svg/round/close.svg"
import DeleteBinRound from "@material-design-icons/svg/round/delete.svg"
import EditPenRound from "@material-design-icons/svg/round/edit.svg"
import NotificationsRound from "@material-design-icons/svg/round/notifications.svg"
import SyncRound from "@material-design-icons/svg/round/sync.svg"
import SearchRound from "@material-design-icons/svg/round/search.svg"
import PersonAddRound from "@material-design-icons/svg/round/person_add.svg"
import ContentCopyRound from "@material-design-icons/svg/round/content_copy.svg"
import ChevronLeftOutlined from "@material-design-icons/svg/outlined/chevron_left.svg"
import ChevronRightOutlined from "@material-design-icons/svg/outlined/chevron_right.svg"
import CloseOutlined from "@material-design-icons/svg/outlined/close.svg"
import NotificationsNoneOutlined from "@material-design-icons/svg/outlined/notifications_none.svg"
import VisibilityOutlined from "@material-design-icons/svg/outlined/visibility.svg"
import VisibilityOffOutlined from "@material-design-icons/svg/outlined/visibility_off.svg"
import EditPenOutlined from "@material-design-icons/svg/outlined/edit.svg"
import MoreVertRound from "@material-design-icons/svg/round/more_vert.svg"
import MoreHorizRound from "@material-design-icons/svg/round/more_horiz.svg"
import GroupAddFilled from "@material-design-icons/svg/filled/group_add.svg"
import ExpandMoreRound from "@material-design-icons/svg/round/expand_more.svg"
import ExpandLessRound from "@material-design-icons/svg/round/expand_less.svg"
import WarningAmberRound from "@material-design-icons/svg/round/warning_amber.svg"
import ManageAccountsFilled from "@material-design-icons/svg/filled/manage_accounts.svg"
/**
 * Object keys correspond to the names found [here](https://marella.me/material-design-icons/demo/svg/)
 **/
export type MaterialIconsType = {
    [ key in 'filled' | 'outlined' | 'round' ]: {
        [key: string]: string
    }
}
export const materialIcons: MaterialIconsType = {
    filled: {
        'account_box': AccountBoxFilled,
        'add_circle': AddCircleFilled,
        'contacts': ContactsFilled,
        'edit': EditFilled,
        'sensors': SensorsFilled,
        'history': HistoryFilled,
        'logout': LogoutFilled,
        'people': PeopleFilled,
        'add_group': GroupAddFilled,
        'manage_accounts': ManageAccountsFilled,
    },
    outlined: {
        'chevron_left': ChevronLeftOutlined,
        'chevron_right': ChevronRightOutlined,
        'close': CloseOutlined,
        'notifications_none': NotificationsNoneOutlined,
        'visibility': VisibilityOutlined,
        'visibility_off': VisibilityOffOutlined,
        'edit': EditPenOutlined,
    },
    round: {
        'account_circle': AccountCircleRound,
        'add': AddRound,
        'close': CloseRound,
        'delete': DeleteBinRound,
        'edit': EditPenRound,
        'notifications': NotificationsRound,
        'search': SearchRound,
        'sync': SyncRound,
        'person_add': PersonAddRound,
        'content_copy': ContentCopyRound,
        'more_vert': MoreVertRound,
        'more_horiz': MoreHorizRound,
        'expand_more': ExpandMoreRound,
        'expand_less': ExpandLessRound,
        'warning_amber': WarningAmberRound,
    },
}

import { type IconDefinition, type IconPack } from "@fortawesome/free-regular-svg-icons"
import { faBars, faXmark } from "@fortawesome/free-solid-svg-icons"
export const fontAwesomeIcons: { [key: string]: IconPack | IconDefinition } = {
    fas: {
        'xmark': faXmark,
        'bars': faBars,
    },
}
