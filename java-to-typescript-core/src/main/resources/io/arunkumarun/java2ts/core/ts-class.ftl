[#ftl]
[#list tsClasses as tsClass]
class ${tsClass.className} {
    [#list tsClass.fields as field]
    ${field.fieldName}: ${field.fieldType};
    [/#list]

    constructor(data: ${tsClass.className}) {
        [#list tsClass.fields as field]
        this.${field.fieldName} = data.${field.fieldName};
        [/#list]
    }
}
[/#list]
