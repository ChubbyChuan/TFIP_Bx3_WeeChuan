export interface Company {
    value: string;
    viewValue: string;
}

export interface Work_Area {
    name: string;
    label: string;
}

export interface PPE {
    name: string;
    label: string;
}

export interface PRECAUTION {
    name: string;
    label: string;
}

export interface Approval {
    selectedWorkArea: String[];
    selectedPPE: String[];
    selectedPrecaution: String[];
    name: String;
    company:String;
    email: String;
}



/* Model for Request + User*/
export interface Request {
    type: string
    name: string
    company: string
    equipment: string
    startdate: Date
    enddate: Date
    locations: string
    comment: string
    email: string
}

export interface Permit {
    id: number
    type: string
    name: string
    company: string
    equipment: string
    startdate: Date
    enddate: Date
    locations: string
    comment: string
    status: string
}

export interface SearchQuery {
    // name: string
    type: string
    locations: string
    status: string
    // page: number
    // limit: number
}

export interface User {
    email: string
    password: string
}


export interface User_Registeration {
    name: string
    email: string
    password: string
    company: string
}
/* model for chart */


// export interface Chart_Data {
//     type: String
//     data: {
//         labels: String[]
//         datasets: [{
//             data: number[]
//         }]
//     }
// }

export interface Chart_Data_Donut {
    type: string;
    data: {
        labels: string[],
        datasets: [{
            data: number[],
        }]
    }
    options: {
        plugins: {
            datalabels: {
                backgroundColor: string,
                borderRadius: number,
                font: {
                    size: number,
                    color: string,
                    weight: string,
                },
            },
            doughnutlabel: {
                labels: {
                    text: string,
                    font: {
                        size: number,
                        weight: string,
                    }
                }
            }
        }
    }
}
export interface Chart_Data {
    type: string;
    data: {
        labels: string[],
        datasets: [{
            label: String,
            data: number[],
        }]
    }}