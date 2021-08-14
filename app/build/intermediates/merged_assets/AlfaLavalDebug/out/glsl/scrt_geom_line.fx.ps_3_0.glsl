precision mediump float;
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform sampler2D DiffuseTexture;
uniform MaterialInfo Material;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  lowp vec4 tmpvar_2;
  tmpvar_2 = texture2D (DiffuseTexture, xlv_TEXCOORD0);
  tmpvar_1 = ((xlv_COLOR0 * tmpvar_2) * Material.m_DiffuseColor);
  gl_FragData[0] = tmpvar_1;
}

