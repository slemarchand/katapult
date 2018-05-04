<#include "macro.ftl">

[{
    "$user = /user/get-user-by-screen-name": {
        "companyId": ${companyId?c},
        "screenName": <@json_string screenName/>
<#--
        "$userUpdate = /user/update-user": {
            "@userId": "$user.userId",
            "oldPassword": null,
            "newPassword1": null,
            "newPassword2": null,
            "@passwordReset": "$user.passwordReset",
            "reminderQueryQuestion": null,
            "reminderQueryAnswer": null,
<#if screenName??>
            "screenName": <@json_string screenName/>,
<#else>
            "@screenName": "$user.screenName",
</#if>
<#if emailAddress??>
            "emailAddress": <@json_string emailAddress/>,
<#else>
            "@emailAddress": "$user.emailAddress",
</#if>
            "@facebookId": "$user.facebookId",
            "@openId": "$user.facebookId",
<#if portraitBytes?? && portraitBytes?size != 0>
            "portrait": true,
            "portraitBytes": [
    <#list portraitBytes as b>
        ${b?c}<#if !b?is_last>,</#if>
    </#list>
            ],
<#else>
            "portrait": true,
            "portraitBytes": null,
</#if>
            "@languageId": "$user.languageId",
            "@timeZoneId": "$user.timeZoneId",
            "@greeting": "$user.greeting",
            "@comments": "$user.comments",
<#if firstName??>
            "firstName": <@json_string firstName/>,
<#else>
            "@firstName": "$user.firstName",
</#if>
<#if middleName??>
            "middleName": <@json_string middleName/>,
<#else>
            "@middleName": "$user.middleName",
</#if>
<#if lastName??>
            "lastName": <@json_string lastName/>,
<#else>
            "@lastName": "$user.lastName",
</#if>
            "@prefixId": "$user.contact.prefixId",
            "@suffixId": "$user.contact.suffixId",
            "@male": "$user.male",
            "@birthdayMonth": "$user.birthday.month",
            "@birthdayDay": "$user.birthday.day",
            "@birthdayYear": "$user.birthday.year",
            "@smsSn":  "$user.contact.smsSn",
            "@facebookSn": "$user.contact.facebookSn",
            "@jabberSn": "$user.contact.jabberSn",
            "@skypeSn": "$user.contact.skypeSn",
            "@twitterSn": "$user.contact.twitterSn",
<#if jobTitle??>
            "jobTitle": <@json_string jobTitle/>,
<#else>
            "@jobTitle": "$user.jobTitle",
</#if>
            "groupIds": null,
            "organizationIds": null,
            "roleIds": null,
            "userGroupRoles": null,
            "userGroupIds": null,
            "addresses": [],
            "emailAddresses": [],
<#if phones?size != 0>
            "phones": [
        <#list phones as phone>
                {
                    "extension": <@json_string phone.extension/>,
                    "number": <@json_string phone.number/>,
                    "typeId": ${phone.typeId?c},
                    "primary": ${phone.primary?c}
                }<#if !phone?is_last>,</#if>
        </#list>
            ],
<#else>
            "phones": null,
</#if>
            "websites": null,
            "announcementsDelivers": null,
            "serviceContext": {
                "expandoBridgeAttributes": {
        <#list expandoBridgeAttributes?keys as key>
        <@json_string key/>: <@json_string expandoBridgeAttributes[key]/><#if !key?is_last>,</#if>
        </#list>
                }
            }
        },
        "$agreedTermsOfUseUpdate = /user/update-agreed-to-terms-of-use": {
            "@userId": "$user.userId",
            "agreedToTermsOfUse": true
       },
             -->
<#if portraitBytes??>,
        "$portraitUpdate = /user/update-portrait": {
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