<#include "macro.ftl">

[{
    "/user/update-user": {
        "userId": ${userId?c},
        "oldPassword": null,
        "newPassword1": null,
        "newPassword2": null,
        "passwordReset": ${passwordReset?c},
        "reminderQueryQuestion": null,
        "reminderQueryAnswer": null,
        "screenName": <#if screenName??><@json_str screenName/><#else>null</#if>,
        "emailAddress": <#if emailAddress??><@json_str emailAddress/><#else>null</#if>,
        "facebookId": ${facebookId?c},
        "openId": <#if openId??><@json_str openId/><#else>null</#if>,
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
        "languageId": <#if timeZoneId??><@json_string timeZoneId/><#else>null</#if>,
        "timeZoneId": <#if timeZoneId??><@json_string timeZoneId/><#else>null</#if>,
        "greeting": <#if greeting??><@json_string greeting/><#else>null</#if>,
        "comments": <#if comments??><@json_string comments/><#else>null</#if>,
        "firstName": <#if firstName??><@json_string firstName/><#else>null</#if>,
        "middleName": <#if middleName??><@json_string middleName/><#else>null</#if>,
        "lastName": <#if lastName??><@json_string lastName/><#else>null</#if>,
        "prefixId": 0,
        "suffixId": 0,
        "male": ${contact.male?c},
        "birthdayMonth": ${contact.birthday?string.@month},
        "birthdayDay": ${contact.birthday?string.@day},
        "birthdayYear": ${contact.birthday?string.@year},
        "smsSn":  "",
        "facebookSn": "",
        "jabberSn": "",
        "skypeSn": "",
        "twitterSn": "",
        "jobTitle": <#if jobTitle??><@json_string jobTitle/><#else>null</#if>,
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
    }
},{
   "/user/update-agreed-to-terms-of-use": {
        "userId": ${userId?c},
        "agreedToTermsOfUse": ${agreedToTermsOfUse?c}
    }
}]