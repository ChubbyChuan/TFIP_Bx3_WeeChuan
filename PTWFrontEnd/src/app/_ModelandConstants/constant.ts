import { Company, PPE, PRECAUTION, Work_Area } from "./model";

export const COMPANIES: Company[] = [
  { value: 'gsk-0', viewValue: 'GSK' },
  { value: 'pec-1', viewValue: 'PEC' },
  { value: 'onyo-2', viewValue: 'ONYO' },
  { value: 'jacobs-3', viewValue: 'Jacobs' },
  { value: 'zenith-4', viewValue: 'Zenith' },
  { value: 'Okura-5', viewValue: 'Okura' },
  { value: 'Others-6', viewValue: 'Others' },
];

export const OPTIONS: string[] = [
  'GSK',
  'PEC',
  'ONYO',
  'Jacob',
  'Zenith',
  'Okura',
  'ISS'
];

export const LOCATIONS: string[] = [
  'Production',
  'Solvent Recovery',
  'Warehouse',
  'Cafeteria',
  'Gents',
  'Ladies'
];

export const WORK_AREA_LIST: Work_Area []= [
  { name: 'gasFume', label: 'GAS FUME' },
  { name: 'dustPowder', label: 'DUST & POWDER' },
  { name: 'flammableLiquid', label: 'FLAMMABLE LIQUID' },
  { name: 'asbestos', label: 'ASBESTOS' },
  { name: 'fallingObject', label: 'FALLING OBJECT' },
  { name: 'vented', label: 'VENTED' },
  { name: 'drained', label: 'DRAINED' }
];

export const PPE_LIST: PPE []= [
  { name: 'safetyGlass', label: 'SAFETY GLASS' },
  { name: 'safetyFootwear', label: 'SAFETY FOOTWEAR' },
  { name: 'safetyHelmet', label: 'SAFETY HELMET' },
  { name: 'safetyWorkwear', label: 'SAFETY WORKWEAR' },
  { name: 'handgloves', label: 'HANDGLOVES' },
  { name: 'n95', label: 'N95' },
]

export const PRECAUTION_LIST: PRECAUTION []= [
  { name: 'waterSpraying', label: 'WATER SPRAYING' },
  { name: 'fireBlanket', label: 'FIRE BLANKET' },
  { name: 'areaBarricade', label: 'AREA BARRICADE' },
  { name: 'provideSafetyAccess', label: 'PROVIDE SAFETY ACCESS' },
  { name: 'displayDangerWarningSigns', label: 'DISPLAY DANGER/WARNING SIGNS' },
  { name: 'additionalVenting', label: 'ADDITIONAL VENTING' }
]

// export const URL_link : String = 'http://localhost:8080'
export const URL_link : String = 'https://ptwbackend-production.up.railway.app'



