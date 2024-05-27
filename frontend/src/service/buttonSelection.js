export function handleCheckboxes(parentCheckbox, childCheckboxes) {
    const parentCheckboxCheckedEvent = () => {
        const isChecked = parentCheckbox.checked;
        childCheckboxes.forEach(childCheckbox => {
            childCheckbox.checked = isChecked;
        });
    };

    const childCheckboxCheckedEvent = () => {
        const allChecked = Array.from(childCheckboxes).every(childCheckbox => childCheckbox.checked);
        parentCheckbox.checked = allChecked;
    };

    parentCheckbox.addEventListener("change", parentCheckboxCheckedEvent);

    childCheckboxes.forEach(childCheckbox => {
        childCheckbox.addEventListener("change", childCheckboxCheckedEvent);
    });
}