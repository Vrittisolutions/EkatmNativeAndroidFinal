#extension GL_EXT_shader_texture_lod : enable
#extension GL_OES_standard_derivatives : enable
lowp vec4 impl_low_texture2DLodEXT(lowp sampler2D sampler, highp vec2 coord, mediump float lod)
{
#if defined(GL_EXT_shader_texture_lod)
	return texture2DLodEXT(sampler, coord, lod);
#else
	return texture2D(sampler, coord, lod);
#endif
}

precision mediump float;
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform highp vec4 ContourColor;
uniform highp vec4 ContourParamsA;
uniform highp vec4 HeightMapSize;
uniform sampler2D HeightMapTexture;
uniform MaterialInfo Material;
uniform sampler2D DiffuseTexture;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_COLOR0;
void main ()
{
  highp vec4 diffuseTextureColor_1;
  highp vec4 baseColor_2;
  highp float tmpvar_3;
  highp vec2 tmpvar_4;
  tmpvar_4 = mix (HeightMapSize.zw, (vec2(1.0, 1.0) - HeightMapSize.zw), xlv_TEXCOORD0.xy);
  lowp vec4 tmpvar_5;
  tmpvar_5 = impl_low_texture2DLodEXT (HeightMapTexture, tmpvar_4, 0.0);
  tmpvar_3 = tmpvar_5.x;
  highp vec2 tmpvar_6;
  tmpvar_6.y = 0.0;
  tmpvar_6.x = tmpvar_3;
  lowp vec4 tmpvar_7;
  tmpvar_7 = texture2D (DiffuseTexture, tmpvar_6);
  diffuseTextureColor_1 = tmpvar_7;
  baseColor_2 = (Material.m_DiffuseColor * diffuseTextureColor_1);
  baseColor_2 = (baseColor_2 * xlv_COLOR0);
  highp float fH_8;
  fH_8 = ((tmpvar_3 + ContourParamsA.z) * ContourParamsA.y);
  highp float tmpvar_9;
  tmpvar_9 = (ContourParamsA.x + 0.5);
  highp float tmpvar_10;
  tmpvar_10 = (abs(dFdx(fH_8)) + abs(dFdy(fH_8)));
  highp float tmpvar_11;
  tmpvar_11 = clamp ((abs(
    fract(fH_8)
  ) / (tmpvar_10 * tmpvar_9)), 0.0, 1.0);
  highp float tmpvar_12;
  tmpvar_12 = clamp ((abs(
    fract(-(fH_8))
  ) / (tmpvar_10 * tmpvar_9)), 0.0, 1.0);
  highp vec4 tmpvar_13;
  tmpvar_13 = mix (baseColor_2, ContourColor, vec4(((1.0 - 
    ((tmpvar_11 * (tmpvar_11 * (3.0 - 
      (2.0 * tmpvar_11)
    ))) * (tmpvar_12 * (tmpvar_12 * (3.0 - 
      (2.0 * tmpvar_12)
    ))))
  ) * ContourColor.w)));
  gl_FragData[0] = tmpvar_13;
}

