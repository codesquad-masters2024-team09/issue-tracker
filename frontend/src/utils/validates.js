import * as yup from 'yup'

export const milestoneValidate = yup.object().shape({
    id: yup.string().required("마일스톤 이름을 입력해 주세요.")
        .max(30, "마일스톤 이름은 최대 30자까지만 입력할 수 있습니다.")
        .label("마일스톤 이름"),
    dueDate: yup.date().nullable(),
    description: yup.string()
        .nullable()
        .max(50, "설명은 최대 50자까지만 입력할 수 있습니다."),
})