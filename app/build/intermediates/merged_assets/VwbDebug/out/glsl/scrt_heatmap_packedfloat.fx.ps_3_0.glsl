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
uniform highp vec4 PackedFloatParams;
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
  highp vec2 tmpvar_3;
  tmpvar_3 = mix (HeightMapSize.zw, (vec2(1.0, 1.0) - HeightMapSize.zw), xlv_TEXCOORD0.xy);
  lowp vec4 tmpvar_4;
  tmpvar_4 = impl_low_texture2DLodEXT (HeightMapTexture, tmpvar_3, 0.0);
  highp float tmpvar_5;
  highp vec4 rgba_6;
  rgba_6 = tmpvar_4.zyxw;
  tmpvar_5 = ((dot (rgba_6.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x);
  highp vec2 tmpvar_7;
  tmpvar_7.y = 0.0;
  tmpvar_7.x = tmpvar_5;
  lowp vec4 tmpvar_8;
  tmpvar_8 = texture2D (DiffuseTexture, tmpvar_7);
  diffuseTextureColor_1 = tmpvar_8;
  baseColor_2 = (Material.m_DiffuseColor * diffuseTextureColor_1);
  baseColor_2 = (baseColor_2 * xlv_COLOR0);
  highp float fH_9;
  fH_9 = ((tmpvar_5 + ContourParamsA.z) * ContourParamsA.y);
  highp float tmpvar_10;
  tmpvar_10 = (ContourParamsA.x + 0.5);
  highp float tmpvar_11;
  tmpvar_11 = (abs(dFdx(fH_9)) + abs(dFdy(fH_9)));
  highp float tmpvar_12;
  tmpvar_12 = clamp ((abs(
    fract(fH_9)
  ) / (tmpvar_11 * tmpvar_10)), 0.0, 1.0);
  highp float tmpvar_13;
  tmpvar_13 = clamp ((abs(
    fract(-(fH_9))
  ) / (tmpvar_11 * tmpvar_10)), 0.0, 1.0);
  highp vec4 tmpvar_14;
  tmpvar_14 = mix (baseColor_2, ContourColor, vec4(((1.0 - 
    ((tmpvar_12 * (tmpvar_12 * (3.0 - 
      (2.0 * tmpvar_12)
    ))) * (tmpvar_13 * (tmpvar_13 * (3.0 - 
      (2.0 * tmpvar_13)
    ))))
  ) * ContourColor.w)));
  gl_FragData[0] = tmpvar_14;
}

