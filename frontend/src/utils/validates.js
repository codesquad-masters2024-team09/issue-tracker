import * as yup from 'yup'

export const milestoneValidate = yup.object().shape({
    id: yup.string().required("마일스톤 이름을 입력해 주세요.")
        .max(30, "마일스톤 이름은 최대 30자까지만 입력할 수 있습니다.")
        .label("마일스톤 이름"),
    dueDate: yup.date().nullable(),
    description: yup.string()
        .nullable()
        .max(50, "설명은 최대 50자까지만 입력할 수 있습니다."),
});

export const labelValidate = yup.object().shape({
    labelId: yup.string().required("레이블 이름을 입력해 주세요.")
        .max(20, "레이블 이름은 최대 20자까지만 입력할 수 있습니다.")
        .label("레이블 이름"),
    description: yup.string()
        .max(50, "설명은 최대 50자까지만 입력할 수 있습니다."),
    textColor: yup.string().required("글자 색을 선택해 주세요.")
        .min(7, "#을 포함한 7자리 색상코드여야 합니다.")
        .max(7, "#을 포함한 7자리 색상코드여야 합니다.")
        .label("글자 색"),
    colorCode: yup.string().required("레이블 색을 입력해 주세요.")
        .max(7, "#을 포함한 7자리 색상코드여야 합니다.")
        .min(7, "#을 포함한 7자리 색상코드여야 합니다.")
        .label("레이블 색")
})