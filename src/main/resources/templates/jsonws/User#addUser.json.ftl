<#include "macro.ftl">
[{
    "$user = /user/add-user": {
        "companyId": ${companyId?c},
        "autoPassword": ${(!password?exists)?c},
        "password1": <#if password??><@json_str password/><#else>null</#if>,
        "password2": <#if password??><@json_str password/><#else>null</#if>,
        "autoScreenName": ${(!screenName?exists)?c},
        "screenName": <#if screenName??><@json_str screenName/><#else>null</#if>,
        "emailAddress": <#if emailAddress??><@json_str emailAddress/><#else>null</#if>,
        "facebookId": ${facebookId?c},
        "openId": <#if openId??><@json_str openId/><#else>null</#if>,
        "locale": "en_US",
        "firstName": <#if firstName??><@json_str firstName/><#else>null</#if>,
        "middleName": <#if middleName??><@json_str middleName/><#else>null</#if>,
        "lastName": <#if lastName??><@json_str lastName/><#else>null</#if>,
        "prefixId": ${contact.prefixId?c},
        "suffixId": ${contact.suffixId?c},
        "male": ${contact.male?c},
        "birthdayMonth": ${contact.birthday?string.@month},
        "birthdayDay": ${contact.birthday?string.@day},
        "birthdayYear": ${contact.birthday?string.@year},
        "jobTitle": <#if contact.jobTitle??><@json_str contact.jobTitle/><#else>null</#if>,
        "groupIds": null,
        "organizationIds": null,
        "roleIds": null,
        "userGroupIds": null,
        "addresses": [],
        "emailAddresses": [],
        "phones": [
        <#list phones as phone>
            {
                "extension": <@json_str phone.extension/>,
                "number": <@json_str phone.number/>,
                "typeId": ${phone.typeId?c},
                "primary": ${phone.primary?c}
            }<#if !phone?is_last>,</#if>
        </#list>
        ],
        "websites": [],
        "announcementsDelivers": [],
        "sendEmail": false,
        "serviceContext": {
            "expandoBridgeAttributes": {
            <#list expandoBridgeAttributes?keys as key>
                <@json_str key/>: <@json_str expandoBridgeAttributes[key]/><#if !key?is_last>,</#if>
            </#list>
            }
        },
        "$agreed = /user/update-agreed-to-terms-of-use": {
            "@userId": "$user.userId",
            "agreedToTermsOfUse": ${agreedToTermsOfUse?c}
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