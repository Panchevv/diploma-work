<template>
<main :key="accountId" :class="pickCssClass('content-wrapper', $style)">
    <div class="d-flex flex-column">
        <TheHeader show-heading :toolbar-items="toolbarItems" />
        <section :class="extractCssClass('thresholds-view', $style)">
            <div style="display: flex; justify-content: flex-end;">
                <h1 style="flex-grow: 1;" :class="extractCssClass('thresholds-heading', $style)">
                    Manage Thresholds<span v-if="!firstFetch"> ({{ thresholds?.length }}) </span>
                </h1>
                <div>
                    <v-dialog v-model="addThresholdDialog" persistent width="420">
                        <template #activator="{ props: activatorProps }">
                            <v-btn v-if="!fetching" v-bind="activatorProps" color="surface" class="v-btn--icon" @click="addThresholdDialog = true">
                                <SvgIcon :type="IconType.MATERIAL" category="round" name="add" alt="plus" />
                            </v-btn>
                            <span v-if="!fetching" :class="pickCssClass('add-btn-text', $style)">Add Threshold</span>
                        </template>
                        <v-card style="padding: 1.5rem; align-items: center;">
                            <v-card-title>
                                <p style="text-align: center;" class="text-h5">Add Threshold</p>
                                <hr />
                            </v-card-title>
                            <v-form ref="thresholdForm">
                                <div class="d-flex mr-3 ml-3 mt-2 gap-3">
                                    <v-text-field 
                                        v-model="thresholdName"
                                        variant="underlined"
                                        label="Threshold Name" />
                                </div>
                                <div class="d-flex mr-3 ml-3 mt-2 gap-3">
                                    <v-combobox
                                        v-model="groupsNames"
                                        :items="allGroupNames"
                                        variant="underlined"
                                        multiple
                                        chips
                                        label="Activate for" />
                                </div>
                                <div class="d-flex mr-3 ml-3 gap-3">
                                    <v-select
                                        v-model="selectedMeasurementType"
                                        variant="underlined"
                                        label="Measurement Type"
                                        :items="allMeasurmentTypesNames" />
                                </div>
                                <div class="d-flex mr-3 ml-3 gap-3">
                                    <v-select
                                        v-model="selectedMeasurementOperator"
                                        variant="underlined"
                                        style="width: 70%;"
                                        label="Operator"
                                        :items="allMeasurmentOperatorNames" />
                                    <v-text-field
                                        v-model="measurementValue"
                                        variant="underlined"
                                        style="width: 30%;"
                                        label="Value"
                                        type="number" />
                                </div>
                                <v-card-actions style="justify-content: center;">
                                    <v-btn
                                        style="background-color: #707cd4"
                                        color="white"
                                        variant="text"
                                        class="mr-4 w-50"
                                        :disabled="!thresholdName"
                                        :loading="loadingCreateThreshold"
                                        @click="createThreshold">
                                        Add
                                    </v-btn>
                                    <v-btn
                                        color="primary-darken-1"
                                        style="border: 1px solid #707cd4 "
                                        variant="text"
                                        @click="addThresholdDialog = false; thresholdName = ''">
                                        Cancel
                                    </v-btn>
                                </v-card-actions>
                            </v-form>
                        </v-card>
                    </v-dialog>
                </div>
            </div>
            <v-progress-linear v-if="fetching" class="flex-0-1 mt-1" />
            <div v-if="!firstFetch && (getThresholds.data.value?.account.thresholds?.length !== undefined && getThresholds.data.value?.account.thresholds.length !== 0)" style="margin-top: 40px; width: 92vw;display: flex; justify-content: center;">
                <div :class="pickCssClass('search-input-container', $style)">
                    <v-text-field
                        v-model.trim="searchQuery"
                        variant="underlined"
                        type="text"
                        placeholder="Search Thresholds">
                        <SvgIcon :type="IconType.MATERIAL" category="round" name="search" alt="magnifying_glass" />
                    </v-text-field>
                </div>
            </div>
            <div v-if="!firstFetch">
                <div v-if="hasThresholds" :class="pickCssClass('grid-container', $style)">
                    <div v-for="threshold in paginatedThresholds" :key="threshold.id" :class="extractCssClass('card', $style)">
                        <h2 :class="extractCssClass('card__title', $style)" style="display: flex; justify-content: flex-end; height: auto;">
                            <span style="flex-grow:10; margin-left:5px; text-align: center; ">{{ threshold.name }}</span>
                            <div>
                                <v-dialog v-model="editThresholdDialog" persistent width="420">
                                    <v-card style="padding: 1.5rem; align-items: center;">
                                        <div style="display: flex; justify-content: center;">
                                            <span style="font-weight: bold; font-size: large" class="mt-3"> Edit Threshold</span>
                                        </div>
                                        <v-form ref="editThresholdForm">
                                            <div class="d-flex mr-3 ml-3 mt-2 gap-3">
                                                <v-text-field 
                                                    v-model="thresholdName"
                                                    variant="underlined"
                                                    label="Threshold Name" />
                                            </div>
                                            <div class="d-flex mr-3 ml-3 mt-2 gap-3">
                                                <v-combobox
                                                    v-model="groupsNames"
                                                    :items="allGroupNames"
                                                    variant="underlined"
                                                    multiple
                                                    chips
                                                    label="Activate For" />
                                            </div>
                                            <div class="d-flex mr-3 ml-3 mt-2 gap-3">
                                                <v-select
                                                    v-model="selectedMeasurementType"
                                                    variant="underlined"
                                                    label="Measurement Type"
                                                    :items="allMeasurmentTypesNames" />
                                            </div>
                                            <div class="d-flex mr-3 ml-3 gap-3">
                                                <v-select
                                                    v-model="selectedMeasurementOperator"
                                                    variant="underlined"
                                                    style="width: 70%;"
                                                    label="Operator"
                                                    :items="allMeasurmentOperatorNames" />
                                                <v-text-field
                                                    v-model="measurementValue"
                                                    variant="underlined"
                                                    style="width: 30%;"
                                                    label="Value"
                                                    type="number" />
                                            </div>
                                            <v-card-actions style="justify-content: center;">
                                                <v-btn
                                                    :disabled="!thresholdName"
                                                    :loading="loadingEditThreshold"
                                                    style="background-color: #707cd4"
                                                    color="white"
                                                    variant="text"
                                                    class="mr-4 w-50"
                                                    @click="editThreshold(threshold.id)">
                                                    Save
                                                </v-btn>
                                                <v-btn
                                                    color="primary-darken-1"
                                                    style="border: 1px solid #707cd4 "
                                                    variant="text"
                                                    @click="clearForm; editThresholdDialog = false;">
                                                    Cancel
                                                </v-btn>
                                            </v-card-actions>
                                        </v-form>
                                    </v-card>
                                </v-dialog>
                            </div>

                            <v-menu open-on-click>
                                <template #activator="{ props: activatorProps }">
                                    <v-btn :loading="selectedThresholdId === threshold.id ? loadingDeleteThreshold : false" :disabled="editThresholdDialog" variant="text" style="flex-grow: 1;position: absolute; top:1px" v-bind="activatorProps">
                                        <SvgIcon class="icon-white"  :type="IconType.MATERIAL" category="round" name="more_vert" alt="menu" />
                                    </v-btn>
                                </template>

                                <v-list style="border-radius: 20px;">
                                    <v-list-item v-for="actionButton in actionButtons" :key="actionButton.id" style="background-color: #808080;">
                                        <v-btn
                                            v-ripple
                                            variant="plain"
                                            :disabled="!actionButton.enabled"
                                            @click="handleActionButtonClick(actionButton.id, threshold)">
                                            <SvgIcon class="icon-white" v-bind="actionButton.icon" />
                                            <span style="color: white"> {{ _.capitalize(actionButton.name) }}</span>
                                        </v-btn>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </h2>
                        <section style="padding: 15px;">
                            <div style="margin-bottom: 10px;">
                                <span style="font-size: 14px; color: grey;">Name: </span>
                                <span style="font-size: 17px; font-weight: bold;">{{ threshold.name }}</span>
                            </div>
                            <div style="margin-bottom: 10px;">
                                <span style="font-size: 14px; color: grey;">Active For: </span>
                                <span style="font-size: 17px; font-weight: bold;">{{ selectedGroupNames(threshold.groupIds).join(', ') }}</span>
                            </div>
                            <div style="margin-bottom: 10px;">
                                <span style="font-size: 13px; color: grey;">Measurement Type: </span>
                                <span style="font-size: 15px; font-weight: bold;">{{ getMeasurementType(threshold.measurementType) }}</span>
                            </div>
                            <div style="margin-bottom: 10px;">
                                <span style="font-size: 13px; color: grey;">Notify Upon: </span>
                                <span style="font-size: 15px; font-weight: bold;">{{ getOperatorMessage(threshold.operator) }} {{ threshold.value }} {{ threshold.measurementType !== null ? getMeasurementUnit(threshold.measurementType) : '' }}</span>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
            <div v-if="!hasThresholds && !fetching" style="display: flex; justify-content: center; font-size: 30px; height: 75vh; align-items: center;"><span style="margin-bottom: 20vh;">No Thresholds.</span></div>
            <v-dialog v-model="confirmThresholdDelete" width="500" height="500">
                <v-card style="padding: 1rem; align-items: center;">
                    <div style="height: 160px; width: 150px; display: flex; justify-content: center; align-items: center;">
                        <SvgIcon :type="IconType.MATERIAL" class="icon-color--error" style="transform: scale(6); margin-bottom: 20px;" category="round" name="warning_amber" alt="warning" />
                    </div>
                    <v-card-title>
                        <p style="text-align: center; white-space: wrap;" class="text-h5">
                            Are you sure you want to delete 
                            <span style="font-weight: bold;">"{{ selectedThresholdName }}</span>"?
                        </p>
                    </v-card-title>
                    <v-card-actions style="width: 60%;display: flex; justify-content: space-evenly; margin-top: 20px">
                        <v-btn
                            style="border: 1.5px solid #b0b0b0; background-color: white; color: #9d9d9d; padding: 20px; align-content: center;" 
                            variant="text"
                            @click="confirmThresholdDelete = false">
                            Cancel
                        </v-btn>
                        <v-btn
                            class="bg-error"
                            style=" padding: 20px; align-content: center;"
                            color="white"
                            variant="text"
                            :loading="loadingDeleteThreshold"
                            @click="deleteThreshold(selectedThresholdId)">
                            Delete
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <div v-if="!firstFetch && hasThresholds" :class="extractCssClass('pagination-controls', $style)">
                <v-btn style="margin-right:5px" :disabled="currentPage === 1" @click="goToFirstPage"><v-icon>mdi-chevron-double-left</v-icon></v-btn>
                <v-btn style="margin-right:5px" :disabled="currentPage === 1" @click="prevPage"><v-icon>mdi-chevron-left</v-icon></v-btn>
                <span v-for="page in displayedPages" :key="page">
                    <v-btn style="transition: 0.5s;" :color="currentPage === page ? '#707cd4' : ''" :width="currentPage === page ? '90px' : '40px'" @click="goToPage(page)">{{ page }}</v-btn>
                </span>
                <v-btn style="margin-right:5px" :disabled="currentPage === totalPages" @click="nextPage"><v-icon>mdi-chevron-right</v-icon></v-btn>
                <v-btn style="margin-right:5px" :disabled="currentPage === totalPages" @click="goToLastPage"><v-icon>mdi-chevron-double-right</v-icon></v-btn>
            </div>
        </section>
    </div>
</main>
</template>

<script setup lang="ts">
import { pickCssClass, extractCssClass } from "@/utils"
import { useUserStore } from "@/stores/UserStore"
import { storeToRefs } from "pinia";
import { IconType, type SvgIconProps } from "@/components/SvgIcon.vue";
import TheHeader, { ToolbarItemType, type ToolbarItem, DropdownItemType } from "@/components/TheHeader.vue"
import { type GetThresholdsQuery, type GetThresholdsQueryVariables, GetThresholdsDocument, type EditThresholdMutation, type EditThresholdMutationVariables, EditThresholdDocument, type DeleteThresholdMutation, type DeleteThresholdMutationVariables, type CreateThresholdMutation, type CreateThresholdMutationVariables, CreateThresholdDocument, DeleteThresholdDocument, MeasurementType, ThresholdOperator } from "@/generated/graphql";
import _ from "lodash";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { useMutation, useQuery, type OperationResult } from "@urql/vue";
import { watchEffect } from "vue";
import SvgIcon from "@/components/SvgIcon.vue"
import { toast } from "vuetify-sonner"
import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups";


const { keycloak, logout } = useUserStore()
const router = useRouter()
const { accountId } = storeToRefs(useUserStore())

const getThresholds = useQuery<GetThresholdsQuery, GetThresholdsQueryVariables>({
    query: GetThresholdsDocument,
    variables: {
        id: accountId?.value!,
    },
    pause: computed(() => accountId == null),
})

const filteredThresholds = computed(() => {
    const query = searchQuery.value.trim().toLowerCase();
    return thresholds.value?.filter((threshold: { name: string; }) => threshold.name.toLowerCase().includes(query)
    ).sort((a, b) => (new Date(b.createdAt) as any) - (new Date(a.createdAt) as any)) || [];
});
const currentPage = ref(1);
let itemsPerPage: number = calculateItemsPerPage(); 

const firstFetch = ref(true)
const hasThresholds = computed(() =>  filteredThresholds.value.length !== 0)
const thresholds = computed(() => getThresholds.data.value?.account.thresholds)
const fetching = computed(() => getThresholds.fetching.value)
const searchQuery = ref('');
const addThresholdDialog = ref<boolean>(false)
const editThresholdDialog = ref<boolean>(false)
const thresholdName = ref<string>("")
const editedThreshold = ref<string>("")
const loadingEditThreshold = ref<boolean>(false)
const loadingCreateThreshold = ref<boolean>(false)
const loadingDeleteThreshold = ref<boolean>(false)
const confirmThresholdDelete = ref<boolean>(false)
const loadingThreshold = ref<boolean>(false)
const groupsNames = ref<any>([])
const selectedMeasurementType = ref<string>();
const selectedMeasurementOperator = ref<string>();
const measurementValue = ref<string>("");
const editFormVisible = ref(false)
const selectedThreshold = ref('')
const validGroups = ref<string[]>([])
const invalidGroups = ref<string[]>([])
const thresholdForm = ref<any>(null);
const editThresholdForm = ref<any>(null);

const { deviceGroupsListItems } = useDeviceGroupsInfo()

const measurementUnits = {
    BATTERY_VOLTAGE: "Millivolts",
    AIR_PRESSURE: "kPa",
    HUMIDITY: "%",
    TEMPERATURE: "°C",
    RSRP: "dBm",
} as const;

const getMeasurementUnit = (measurementType: MeasurementType): string => {
    return measurementUnits[measurementType] ?? '';
};

const selectedGroupNames = (groupIds: any) => {
    return groupIds.map((selectedGroupId: string | number) => {
        const foundGroup = deviceGroupsListItems.value.find(group => group.id === selectedGroupId);        
        return foundGroup ? foundGroup.name : null;
    })
};

const measurmentOperator = ref<any>(Object.keys(ThresholdOperator))
const allMeasurmentOperatorNames = computed(() => {
    return measurmentOperator.value.map((measurmentOperator: any) => {
        return getOperatorMessage(measurmentOperator);
    });
});

const measurementTypes = ref<any>(Object.keys(MeasurementType))
const allMeasurmentTypesNames = computed(() => {
    return measurementTypes.value.map((measurementType: any) => {
        return getMeasurementType(measurementType);
    });
});

const getMeasurementTypeOriginal = (selectedMeasurementType: string | undefined): MeasurementType => {
    switch (selectedMeasurementType) {
        case "Air Pressure":
            return MeasurementType.AIR_PRESSURE;
        case "Temperature":
            return MeasurementType.TEMPERATURE;
        case "Humidity":
            return MeasurementType.HUMIDITY;
        case "Battery Voltage":
            return MeasurementType.BATTERY_VOLTAGE;
        case "Rsrp":
            return MeasurementType.RSRP;
        default:
            return MeasurementType.AIR_PRESSURE;
    }
};

const getMeasurementType = (measurementType: any) => {
    return measurementType === 'AIR_PRESSURE' ? "Air Pressure" :
        measurementType === 'TEMPERATURE' ? "Temperature"  :
        measurementType === 'HUMIDITY' ? "Humidity"  :
        measurementType === 'BATTERY_VOLTAGE' ? "Battery Voltage"  :
        measurementType === 'RSRP' ? "Rsrp"  : 'No measurement type'
}

const getThresholdOperatorOriginal = (selectedMeasurementOperator: string | undefined): ThresholdOperator => {
    switch (selectedMeasurementOperator) {
        case "Greater than":
            return ThresholdOperator.GREATER_THAN;
        case "Less than":
            return ThresholdOperator.LESS_THAN;
        case "Equal to":
            return ThresholdOperator.EQUAL;
        default:
            return ThresholdOperator.GREATER_THAN;
    }
};

const getOperatorMessage = (operator: any) => {
    return operator === 'GREATER_THAN' ? "Greater than" :
        operator === 'LESS_THAN' ? "Less than" :
        operator === 'EQUAL' ? "Equal to" : 'Operator Not Specified';
};

const editThresholdButtonDisabled = computed(() => {
    return !editedThreshold.value || (editedThreshold.value === selectedThresholdName.value)
});

const allGroups = computed(() => deviceGroupsListItems.value);
const allGroupNames = computed(() => 
    allGroups.value.map(group => group.name
    ));

const getSelectedGroupIds = (groupsNames: any): any => {
    return groupsNames.map((selectedGroupName: string) => {        
        const foundGroup = deviceGroupsListItems.value.find(group => group.name === selectedGroupName);
        return foundGroup ? foundGroup.id : null;
    });
};


const groupIds = ref<any>()
const thresholdMeasurmentTypeSelected = ref<MeasurementType>(MeasurementType.AIR_PRESSURE);
const thresholdOperatorSelected = ref<ThresholdOperator>(ThresholdOperator.GREATER_THAN)
watchEffect(() => {    
    groupIds.value = getSelectedGroupIds(groupsNames.value)
    thresholdMeasurmentTypeSelected.value = getMeasurementTypeOriginal(selectedMeasurementType.value)
    thresholdOperatorSelected.value = getThresholdOperatorOriginal(selectedMeasurementOperator.value)
})

watchEffect(() => {
    validGroups.value = groupsNames.value.filter((group: string) => allGroupNames.value.includes(group));
    invalidGroups.value = groupsNames.value.filter((group: string) => !allGroupNames.value.includes(group));  
})

const clearForm = () => {
    addThresholdDialog.value = false;
    editThresholdDialog.value = false;
    thresholdName.value = "";
    groupsNames.value = [];
    selectedMeasurementType.value = undefined;
    selectedMeasurementOperator.value = undefined;
    measurementValue.value = "";
}

const editThresholdFormValues = (threshold: any) => {
    selectedThreshold.value = threshold
    thresholdName.value = threshold.name
    groupsNames.value = selectedGroupNames(threshold.groupIds)
    selectedMeasurementType.value = getMeasurementType(threshold.measurementType)
    selectedMeasurementOperator.value = getOperatorMessage(threshold.operator)
    measurementValue.value = threshold.value
    editThresholdDialog.value = true
}

watchEffect(() => {
    if (hasThresholds.value){
        firstFetch.value = false        
    }
})

const selectedThresholdId = ref('')
const selectedThresholdName = ref('')
const handleActionButtonClick = (actionButtonId: string, threshold: any) => {
    if (actionButtonId === 'editThreshold') {
        editThresholdFormValues(threshold)
    }
    if (actionButtonId === 'deleteThreshold') {  
        confirmThresholdDelete.value = true 
        selectedThresholdId.value = threshold.id 
        selectedThresholdName.value = threshold.name    
    }
}
window.addEventListener('resize', () => {
    itemsPerPage = calculateItemsPerPage();
});

function calculateItemsPerPage() {
    return 8;
}

const totalThresholds = computed(() => filteredThresholds.value.length || 0);
const totalPages = computed(() => Math.ceil(totalThresholds.value / itemsPerPage));

const paginatedThresholds = computed(() => {
    const startIndex = (currentPage.value - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage - 1, filteredThresholds.value.length - 1);
    return filteredThresholds.value.slice(startIndex, endIndex + 1);
});

const nextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++;
    }
};

const prevPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--;
    }
};

const displayedPagesCount = calculatePageIndexes();

window.addEventListener('resize', () => {
    itemsPerPage = calculatePageIndexes();
});

function calculatePageIndexes() {
    const screenWidth = window.innerWidth;

    if (screenWidth <= 900) {
        return 1;
    } else {
        return 3;
    } 
} 
const displayedPagesStartIndex = computed(() => {
    let start = Math.max(currentPage.value - Math.floor(displayedPagesCount / 2), 1);
    const maxStart = totalPages.value - displayedPagesCount + 1;
    start = Math.min(start, maxStart);
    start = Math.max(start, 1);
    return start;
});

const displayedPagesEndIndex = computed(() => Math.min(displayedPagesStartIndex.value + displayedPagesCount - 1, totalPages.value));

const displayedPages = computed(() => {
    const pages = [];
    for (let i = displayedPagesStartIndex.value; i <= displayedPagesEndIndex.value; i++) {
        pages.push(i);
    }
    return pages;
});

const goToPage = (page: any) => {
    currentPage.value = page;
};

const goToFirstPage = () => {
    currentPage.value = 1;
};

const goToLastPage = () => {
    currentPage.value = totalPages.value;
};

const createThresholdMutation = useMutation<CreateThresholdMutation, CreateThresholdMutationVariables>(CreateThresholdDocument)
const createThreshold = () => {
    loadingThreshold.value = true      
    createThresholdMutation.executeMutation({
        accountId: accountId?.value!,
        name: thresholdName.value,
        groupIds: groupIds.value,
        measurementType: thresholdMeasurmentTypeSelected.value,
        operator: thresholdOperatorSelected.value,
        value: measurementValue.value ? parseFloat(measurementValue.value) : 0,
    }).then(({ data, error }: OperationResult<CreateThresholdMutation>) => {
        if (error) {      
            console.error(error)
            loadingThreshold.value = false         
            return
        }
        getThresholds.executeQuery({ requestPolicy: 'network-only' })

        //@ts-ignore
        toast(`Successfully CREATED Threshold "${data?.createThreshold.name}"`, {
            cardProps: {
                color: "success",
            },
        })
        loadingThreshold.value = false         
        clearForm()
    }).catch((error) => {
        loadingThreshold.value = false
        console.error(error)
    })
}
const editThresholdMutation = useMutation<EditThresholdMutation, EditThresholdMutationVariables>(EditThresholdDocument)
const editThreshold = (thresholdId: string) => {
    loadingThreshold.value = true         

    editThresholdMutation.executeMutation({
        accountId: accountId?.value!,
        id: thresholdId,
        name: thresholdName.value,
        groupIds: groupIds.value,
        measurementType: thresholdMeasurmentTypeSelected.value,
        operator: thresholdOperatorSelected.value,
        value: measurementValue.value ? parseFloat(measurementValue.value) : 0,

    }).then(({ data, error }: OperationResult<EditThresholdMutation>) => {
        if (error) {                        
            loadingThreshold.value = false         
            return
        }
        getThresholds.executeQuery({ requestPolicy: 'network-only' })
        editThresholdDialog.value = false
        //@ts-ignore
        toast(`Successfully EDITED Threshold "${data?.editThreshold.name}"`, {
            cardProps: {
                color: "warning",
            },
        })
        loadingThreshold.value = false         
        clearForm()
    }).catch((error) => {
        loadingThreshold.value = false         
        console.error(error)
    })
}

const deleteThresholdMutation = useMutation<DeleteThresholdMutation, DeleteThresholdMutationVariables>(DeleteThresholdDocument)
const deleteThreshold = (threshold: any) => {
    loadingDeleteThreshold.value = true         
    deleteThresholdMutation.executeMutation({
        accountId: accountId?.value!,
        id: threshold,
    }).then(({ error }: OperationResult<DeleteThresholdMutation>) => {
        if (error) {                        
            loadingDeleteThreshold.value = false         
            return
        }
        confirmThresholdDelete.value = false
        getThresholds.executeQuery({ requestPolicy: 'network-only' })

        //@ts-ignore
        toast(`Successfully DELETED Threshold "${threshold.name}"`, {
            cardProps: {
                color: "error",
            },
        })
        loadingDeleteThreshold.value = false         

    }).catch((error) => {
        loadingDeleteThreshold.value = false         
        console.error(error)
        
    })
}

const actionButtons: Array<{
    id: string
    name: string
    icon: SvgIconProps
    enabled: boolean
}> = [
    {
        id: "editThreshold",
        name: "Edit Threshold",
        icon: {
            type: IconType.MATERIAL,
            name: "edit",
            category: "round",
            alt: "edit name icon",
        },
        enabled: true,
    },
    {
        id: "deleteThreshold",
        name: "Delete Threshold",
        icon: {
            type: IconType.MATERIAL,
            name: "delete",
            category: "round",
            alt: "delete group icon",
        },
        enabled: true,
    },
]
const toolbarItems = computed<Array<ToolbarItem>>(() => [
    {
        type: ToolbarItemType.DROPDOWN,
        id: "profile",
        color: "primary",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "account_box",
            alt: "Profile menu",
        },
        items: [
            {
                type: DropdownItemType.BUTTON,
                id: "switch_acc",
                name: "Switch Account",
                onClick: () => { router.push("/account-select") },
            },
            {
                type: DropdownItemType.BUTTON,
                id: "logout",
                name: "Logout",
                onClick: () => { logout() },
            },
        ],
        name: keycloak.tokenParsed?.name,
    },
])
</script>

<style lang="scss" module>
@import "../assets/styles/variables.scss";
@import "../assets/styles/global.scss";

.content-wrapper{
    @extend .content-wrapper;
    background-image: url("@/assets/images/background.png");
    background-repeat: repeat;
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow-y: hidden;
}
.heading {
    @media only screen and (min-width: 601px) {  
        display: none;
    }
    margin-top: 100px;
}
.thresholds-heading {
            flex: 0 1 auto;
            margin-bottom: 1rem;
            font-family: Roboto, "Helvetica Neue", sans-serif;
            font-weight: 400;
            font-size: 24px;
            color: rgb(var(--v-theme-on-background));
            @media only screen and (max-width: 600px) { 
                font-size: 24px;
            };
        }
.grid-container {
    display: grid;
    justify-content: center;
    grid-template-columns: auto;
    gap: 30px;
    margin-top: 50px;

    @media only screen and (min-width: 601px) and (max-width: 900px) {
    grid-template-columns: repeat(1, auto); 
    }
    @media only screen and (min-width: 1280px) { 
    grid-template-columns: repeat(2, 25vw);
    }
    @media only screen and (min-width: 1580px) { 
    grid-template-columns: repeat(3, 22vw);
    }
    @media only screen and (min-width: 1900px) { 
    grid-template-columns: repeat(4, 19vw);
    }
    @media only screen and (min-width: 2300px) { 
    grid-template-columns: repeat(5, 15vw);
    }
}
.card {
    position: relative;
    display: flex;
    flex-direction: column;
    height: 13em;
    width: 17vw;
    min-width: 230px;
    border: none;
    border-radius: 10px;
    box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    background-color: white;
    overflow: hidden;

    @media only screen and (max-width: 601px) { 
        min-width: 22.5em;
        }
    @media only screen and (max-width: 430px) { 
        margin-left: 15px;
        min-width:85vw;
    }

    &__title {
        height: 2em;
        flex: 0 1 auto;
        text-align: center;
        padding-top: 0.2em;
        border-radius: 0.2em 0.2em 0 0;
        background-color: rgb(var(--v-theme-secondary));
        color: rgb(var(--v-theme-on-secondary));
    }

    &__body {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        padding: 8px 8px 8px 8px;
    }
    &__description {
        font-weight: bolder;
        color: rgb(var(--v-theme-on-surface));
    }
}
.overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 24px;
    text-align: center;
    &__text {
        font-size: 14px;
        font-weight: bold;
    }
}
.btn {
    box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    width: 9em;
    height: 2em;
    padding: 0;
    font-weight: 600;
    cursor: pointer;
    border-radius: 20px;
    align-self: center;
    color: rgb(var(--v-theme-secondary));
    text-align: center;
    padding-top: 0.25rem;
    text-decoration: none;
}
.btn:hover {
    background-color: rgb(var(--v-theme-primary));
    color: rgb(var(--v-theme-background));
}
.card-container {
    padding-top: 20px;
    padding-bottom: 20px;
    overflow-y: scroll;
    height: 75vh;
    width: 93vw;
    box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    border-radius: 0.5rem;
    min-width: 650px;
    background-color: rgba(245, 245, 245, 0.6);
    @media only screen and (max-width: 600px) {
        min-width: 300px;
    }
}
.thresholds-view{
    @media only screen and (max-width: 600px) {
            margin-top: 90px;
        }
}

.pagination-controls {
    display: flex;
    justify-content: center;
    margin-top: 70px;
    position: fixed; 
    bottom: 3vw; 
    left: 52%; 
    transform: translateX(-50%); 
    z-index: 999; 
}

.pagination-controls v-btn {
    min-width: 40px;
    padding: 4px;
    margin: 0 16px; 
    transition: width 0.3s;

}

.pagination-controls v-btn::before {
    display: none;
}

.pagination-controls v-btn:hover {
    background-color: transparent;
}

.pagination-controls span {
    margin: 0 2px;
}

.search-input-container {
    width: 400px;
    @media only screen and (max-width: 415px) { 
    max-width: 90%;
    }
    @media only screen and (max-width: 376px) { 
    max-width: 80%;
    }
}
.add-btn-text {
        opacity: 0.3;
        margin-left: 2px;
        pointer-events: none;
        cursor: not-allowed;
        margin-left:8px;
        @media only screen and (max-width: 600px) { 
            display: none;
        }
    }
</style>