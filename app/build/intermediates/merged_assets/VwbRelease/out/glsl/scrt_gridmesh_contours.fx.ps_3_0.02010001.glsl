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
struct sGradientParams {
  highp vec4 YMinMaxFactor;
  highp vec4 XMinMaxFactor;
  highp vec4 ZMinMaxFactor;
};
struct sGridParams {
  highp vec4 ContourScale;
  highp vec4 ContourOffset;
  highp vec4 ContourThickness;
  highp vec4 ContourColorX;
  highp vec4 ContourColorY;
  highp vec4 ContourColorZ;
  highp vec4 Params;
  highp vec4 SurfaceNormal;
  highp vec4 Lighting;
};
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform highp vec3 CameraPosition;
uniform sGradientParams GradientMinMax;
uniform sGridParams GridParams;
uniform sampler2D CellInfoTexture;
uniform highp vec4 AmbientLightColor;
uniform MaterialInfo Material;
uniform highp mat4 Light0;
uniform sampler2D DiffuseTexture;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
void main ()
{
  highp vec4 diffuseTextureColor_1;
  highp vec4 baseColor_2;
  highp vec4 finalColor_3;
  highp vec2 x_4;
  x_4 = (vec2(0.5, 0.5) - abs((xlv_TEXCOORD0.xy - vec2(0.5, 0.5))));
  bvec2 tmpvar_5;
  tmpvar_5 = lessThan (x_4, vec2(0.0, 0.0));
  if (any(tmpvar_5)) {
    discard;
  };
  highp vec2 tmpvar_6;
  tmpvar_6.y = 0.0;
  tmpvar_6.x = ((xlv_TEXCOORD6 - GradientMinMax.YMinMaxFactor.x) / (GradientMinMax.YMinMaxFactor.y - GradientMinMax.YMinMaxFactor.x));
  highp vec2 tmpvar_7;
  tmpvar_7 = mix (xlv_TEXCOORD0.xy, tmpvar_6, GridParams.Params.xx);
  highp vec3 tmpvar_8;
  tmpvar_8 = normalize(xlv_TEXCOORD2);
  highp vec3 tmpvar_9;
  tmpvar_9 = normalize((CameraPosition - xlv_TEXCOORD1.xyz));
  lowp vec4 tmpvar_10;
  tmpvar_10 = texture2D (DiffuseTexture, tmpvar_7);
  diffuseTextureColor_1 = tmpvar_10;
  baseColor_2 = (Material.m_DiffuseColor * diffuseTextureColor_1);
  finalColor_3 = baseColor_2;
  highp vec3 result_11;
  highp float tmpvar_12;
  tmpvar_12 = ((xlv_TEXCOORD6 + GridParams.ContourOffset.y) * GridParams.ContourScale.y);
  highp float tmpvar_13;
  tmpvar_13 = (GridParams.ContourThickness.y * 0.5);
  highp float tmpvar_14;
  tmpvar_14 = abs(fract(tmpvar_12));
  highp float tmpvar_15;
  tmpvar_15 = (abs(dFdx(tmpvar_12)) + abs(dFdy(tmpvar_12)));
  highp float tmpvar_16;
  tmpvar_16 = clamp ((tmpvar_14 / (tmpvar_15 * tmpvar_13)), 0.0, 1.0);
  highp float tmpvar_17;
  tmpvar_17 = clamp (((1.0 - tmpvar_14) / (tmpvar_15 * tmpvar_13)), 0.0, 1.0);
  highp vec3 tmpvar_18;
  tmpvar_18.xz = vec2(1.0, 1.0);
  tmpvar_18.y = ((tmpvar_17 * (tmpvar_17 * 
    (3.0 - (2.0 * tmpvar_17))
  )) * (tmpvar_16 * (tmpvar_16 * 
    (3.0 - (2.0 * tmpvar_16))
  )));
  result_11 = tmpvar_18;
  if ((tmpvar_18.y == 0.0)) {
    result_11.y = 1.0;
  };
  lowp vec4 tmpvar_19;
  tmpvar_19 = impl_low_texture2DLodEXT (CellInfoTexture, xlv_TEXCOORD0.zw, 0.0);
  highp vec4 tmpvar_20;
  tmpvar_20 = tmpvar_19;
  bvec4 tmpvar_21;
  tmpvar_21 = bvec4(tmpvar_20);
  bool tmpvar_22;
  tmpvar_22 = any(tmpvar_21);
  highp vec4 tmpvar_23;
  if (tmpvar_22) {
    tmpvar_23 = tmpvar_20;
  } else {
    tmpvar_23 = baseColor_2;
  };
  highp vec4 tmpvar_24;
  tmpvar_24.xyz = Material.m_SpecularColor.xyz;
  tmpvar_24.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_25;
  highp vec4 tmpvar_26;
  tmpvar_26 = (tmpvar_23 * AmbientLightColor);
  highp vec4 v_27;
  v_27.x = Light0[0].y;
  v_27.y = Light0[1].y;
  v_27.z = Light0[2].y;
  v_27.w = Light0[3].y;
  highp vec4 v_28;
  v_28.x = Light0[0].x;
  v_28.y = Light0[1].x;
  v_28.z = Light0[2].x;
  v_28.w = Light0[3].x;
  highp vec3 tmpvar_29;
  tmpvar_29 = normalize(-(v_28.xyz));
  finalColor_25.xyz = (tmpvar_26 + ((
    (tmpvar_23 * max (dot (tmpvar_8, tmpvar_29), 0.0))
   + 
    (tmpvar_24 * pow (clamp (dot (
      (tmpvar_29 - (2.0 * (dot (tmpvar_8, tmpvar_29) * tmpvar_8)))
    , 
      -(normalize(tmpvar_9))
    ), 0.0, 1.0), tmpvar_24.w))
  ) * v_27)).xyz;
  finalColor_25.w = tmpvar_26.w;
  finalColor_3 = (((1.0 - GridParams.Lighting.x) * tmpvar_23) + (GridParams.Lighting.x * clamp (finalColor_25, 0.0, 1.0)));
  finalColor_3 = (finalColor_3 * GridParams.Params.y);
  highp vec4 tmpvar_30;
  tmpvar_30 = mix (finalColor_3, GridParams.ContourColorY, vec4((GridParams.ContourColorY.w * (1.0 - result_11.y))));
  finalColor_3.xyz = tmpvar_30.xyz;
  finalColor_3.w = (tmpvar_30.w * (baseColor_2.w * Material.m_DiffuseColor.w));
  highp vec4 tmpvar_31;
  tmpvar_31.xyz = finalColor_3.xyz;
  tmpvar_31.w = GridParams.Params.y;
  bvec4 tmpvar_32;
  tmpvar_32 = bvec4(tmpvar_31);
  bool tmpvar_33;
  tmpvar_33 = any(tmpvar_32);
  highp float tmpvar_34;
  if (tmpvar_33) {
    tmpvar_34 = (finalColor_3.w - 0.00390625);
  } else {
    tmpvar_34 = -1.0;
  };
  if ((tmpvar_34 < 0.0)) {
    discard;
  };
  gl_FragData[0] = finalColor_3;
}

