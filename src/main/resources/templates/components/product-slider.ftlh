<#macro productslider images>
<#assign x=1>

<div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
    <div class="carousel-inner">
        <#if images??>
            <#list images as image>
                <#if x==1>
                    <div class="carousel-item active">
                        <img class="d-block w-100" src="/img/${image}"
                             alt="">
                    </div>
                    <#assign x++>
                <#else>
                    <div class="carousel-item">
                        <img class="d-block w-100" src="/img/${image}"
                             alt="">
                    </div>
                </#if>
            </#list>
        <#else>
            <div class="carousel-item active">
                <img class="d-block w-100" src="data:image/png;base64,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"
                     alt="First slide">
            </div>
        </#if>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>

<style>img{height: 400px; object-fit: cover;}</style>
</#macro>