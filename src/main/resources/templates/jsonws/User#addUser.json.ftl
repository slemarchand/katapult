<#include "macro.ftl">
[{
    "$user = /user/add-user": {
        "companyId": ${companyId?c},
        "autoPassword": false,
        "password1": <@json_string password/>,
        "password2": <@json_string password/>,
        "autoScreenName": false,
        "screenName": <@json_string screenName/>,
        "emailAddress": <@json_string emailAddress/>,
        "facebookId": 0,
        "openId": "",
        "locale": "en_US",
        "firstName": <@json_string firstName/>,
        "middleName": <@json_string middleName/>,
        "lastName": <@json_string lastName/>,
        "prefixId": 0,
        "suffixId": 0,
        "male": true,
        "birthdayMonth": 1,
        "birthdayDay": 1,
        "birthdayYear": 1970,
        "jobTitle": <@json_string jobTitle/>,
        "groupIds": null,
        "organizationIds": null,
        "roleIds": null,
        "userGroupIds": null,
        "sendEmail": false,
        "serviceContext": {
            "expandoBridgeAttributes": {
            <#list expandoBridgeAttributes?keys as key>
                <@json_string key/>: <@json_string expandoBridgeAttributes[key]/>
            </#list>
            }
        },
        "$agreed = /user/update-agreed-to-terms-of-use": {
            "@userId": "$user.userId",
            "agreedToTermsOfUse": true
        <#if portraitBytes??>
        },
        "$updated = /user/update-portrait": {
            "@userId": "$user.userId",
            "bytes": [
            <#list portraitBytes as b>
                ${b?c}<#if !b?is_last>,</#if>
            </#list>
            ]
        </#if>
        }
    }
}]