<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
<style type="text/css">
.region {
	float: left;
	width: 180px;
	padding: 5px 5px 15px 5px;
}
div.region img {
	width: 160px;
	margin: 0px 0px 15px 0px;
}
.regional-center  {
	font-weight: bold;
}
.republic-center h4 {
	font-weight: bold;
	text-decoration: underline;
}
</style>
			</head>
			<body>
				<xsl:for-each select="republic/region">
					<div class="region">
						<xsl:if test="@republicCenter='true'">
							<xsl:attribute name="class">region republic-center</xsl:attribute>
						</xsl:if>
						<h4><xsl:value-of select="@name"/></h4>
						<img>
							<xsl:attribute name="src">
								<xsl:value-of select="map"/>
							</xsl:attribute>
						</img>
						<xsl:for-each select="city">
							<div class="city">
								<span>
									<xsl:if test="@regionalCenter='true'">
										<xsl:attribute name="class">regional-center</xsl:attribute>
									</xsl:if>
									<xsl:attribute name="title">
										<xsl:value-of select="@OCATO" />
									</xsl:attribute>
									<xsl:value-of select="@type"/>&#160;<xsl:value-of select="@name"/>
								</span>
							</div>
						</xsl:for-each>
					</div>
				</xsl:for-each>
			</body>
		</html>
</xsl:template>
</xsl:stylesheet>