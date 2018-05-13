<#macro json_str str>"${str?js_string?replace("\\'", "\'")?replace("\\>", ">")}"</#macro>
<#macro json_string str><@json_str str/></#macro>
<#macro date_month date>date?string</#macro>
<#macro date_day date></#macro>
<#macro date_year date></#macro>