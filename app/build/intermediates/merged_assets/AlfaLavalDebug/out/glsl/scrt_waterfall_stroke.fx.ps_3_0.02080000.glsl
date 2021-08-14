precision mediump float;
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform sampler2D DiffuseTexture;
uniform MaterialInfo Material;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 diffuseTextureColor_3;
  lowp vec4 tmpvar_4;
  tmpvar_4 = texture2D (DiffuseTexture, xlv_TEXCOORD1);
  diffuseTextureColor_3 = tmpvar_4;
  tmpvar_2.xyz = diffuseTextureColor_3.xyz;
  tmpvar_2.w = (diffuseTextureColor_3.w * min ((
    abs((0.5 - abs((0.5 - xlv_TEXCOORD0.y))))
   / xlv_TEXCOORD0.x), 1.0));
  tmpvar_1 = (tmpvar_2 * Material.m_DiffuseColor);
  gl_FragData[0] = tmpvar_1;
}

