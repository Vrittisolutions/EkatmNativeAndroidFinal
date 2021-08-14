#extension GL_EXT_shader_texture_lod : enable
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
uniform highp mat4 matInvViewProj;
uniform highp vec3 CameraPosition;
uniform highp vec4 screenSize;
uniform sGradientParams GradientMinMax;
uniform sGridParams GridParams;
uniform sampler2D CellInfoTexture;
uniform sampler2D DepthTexture;
uniform highp vec4 AmbientLightColor;
uniform MaterialInfo Material;
uniform highp mat4 Light0;
uniform sampler2D DiffuseTexture;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_TEXCOORD5;
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
  highp vec3 tmpvar_11;
  highp vec2 _screenUV_12;
  _screenUV_12 = (xlv_TEXCOORD5.xy / xlv_TEXCOORD5.w);
  highp float fDepth_13;
  highp vec2 BaseTexCoord_14;
  BaseTexCoord_14.x = _screenUV_12.x;
  BaseTexCoord_14.y = -(_screenUV_12.y);
  BaseTexCoord_14 = ((BaseTexCoord_14 + 1.0) * 0.5);
  BaseTexCoord_14 = (BaseTexCoord_14 + screenSize.zw);
  BaseTexCoord_14.y = (1.0 - BaseTexCoord_14.y);
  lowp float tmpvar_15;
  tmpvar_15 = texture2D (DepthTexture, BaseTexCoord_14).x;
  fDepth_13 = tmpvar_15;
  if ((fDepth_13 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_16;
  tmpvar_16.w = 1.0;
  tmpvar_16.xy = _screenUV_12;
  tmpvar_16.z = fDepth_13;
  highp vec4 tmpvar_17;
  tmpvar_17 = (tmpvar_16 * matInvViewProj);
  highp vec2 tmpvar_18;
  tmpvar_18.y = 0.0;
  tmpvar_18.x = (screenSize.z * 2.0);
  highp vec2 _screenUV_19;
  _screenUV_19 = (_screenUV_12 + tmpvar_18);
  highp float fDepth_20;
  highp vec2 BaseTexCoord_21;
  BaseTexCoord_21.x = _screenUV_19.x;
  BaseTexCoord_21.y = -(_screenUV_19.y);
  BaseTexCoord_21 = ((BaseTexCoord_21 + 1.0) * 0.5);
  BaseTexCoord_21 = (BaseTexCoord_21 + screenSize.zw);
  BaseTexCoord_21.y = (1.0 - BaseTexCoord_21.y);
  lowp float tmpvar_22;
  tmpvar_22 = texture2D (DepthTexture, BaseTexCoord_21).x;
  fDepth_20 = tmpvar_22;
  if ((fDepth_20 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_23;
  tmpvar_23.w = 1.0;
  tmpvar_23.xy = _screenUV_19;
  tmpvar_23.z = fDepth_20;
  highp vec4 tmpvar_24;
  tmpvar_24 = (tmpvar_23 * matInvViewProj);
  highp vec2 tmpvar_25;
  tmpvar_25.x = 0.0;
  tmpvar_25.y = (screenSize.w * 2.0);
  highp vec2 _screenUV_26;
  _screenUV_26 = (_screenUV_12 + tmpvar_25);
  highp float fDepth_27;
  highp vec2 BaseTexCoord_28;
  BaseTexCoord_28.x = _screenUV_26.x;
  BaseTexCoord_28.y = -(_screenUV_26.y);
  BaseTexCoord_28 = ((BaseTexCoord_28 + 1.0) * 0.5);
  BaseTexCoord_28 = (BaseTexCoord_28 + screenSize.zw);
  BaseTexCoord_28.y = (1.0 - BaseTexCoord_28.y);
  lowp float tmpvar_29;
  tmpvar_29 = texture2D (DepthTexture, BaseTexCoord_28).x;
  fDepth_27 = tmpvar_29;
  if ((fDepth_27 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_30;
  tmpvar_30.w = 1.0;
  tmpvar_30.xy = _screenUV_26;
  tmpvar_30.z = fDepth_27;
  highp vec4 tmpvar_31;
  tmpvar_31 = (tmpvar_30 * matInvViewProj);
  highp vec3 tmpvar_32;
  tmpvar_32 = (((tmpvar_17 / tmpvar_17.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz);
  highp vec3 tmpvar_33;
  tmpvar_33 = (abs((
    (((tmpvar_24 / tmpvar_24.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_32)) + abs((
    (((tmpvar_31 / tmpvar_31.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_32)));
  highp vec3 tmpvar_34;
  tmpvar_34 = (GridParams.ContourThickness.xyz * 0.5);
  highp vec3 tmpvar_35;
  tmpvar_35 = clamp ((abs(
    fract(tmpvar_32)
  ) / (tmpvar_33 * tmpvar_34)), 0.0, 1.0);
  highp vec3 tmpvar_36;
  tmpvar_36 = clamp (((1.0 - 
    abs(fract(tmpvar_32))
  ) / (tmpvar_33 * tmpvar_34)), 0.0, 1.0);
  tmpvar_11 = ((tmpvar_36 * (tmpvar_36 * 
    (3.0 - (2.0 * tmpvar_36))
  )) * (tmpvar_35 * (tmpvar_35 * 
    (3.0 - (2.0 * tmpvar_35))
  )));
  lowp vec4 tmpvar_37;
  tmpvar_37 = impl_low_texture2DLodEXT (CellInfoTexture, xlv_TEXCOORD0.zw, 0.0);
  highp vec4 tmpvar_38;
  tmpvar_38 = tmpvar_37;
  bvec4 tmpvar_39;
  tmpvar_39 = bvec4(tmpvar_38);
  bool tmpvar_40;
  tmpvar_40 = any(tmpvar_39);
  highp vec4 tmpvar_41;
  if (tmpvar_40) {
    tmpvar_41 = tmpvar_38;
  } else {
    tmpvar_41 = baseColor_2;
  };
  highp vec4 tmpvar_42;
  tmpvar_42.xyz = Material.m_SpecularColor.xyz;
  tmpvar_42.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_43;
  highp vec4 tmpvar_44;
  tmpvar_44 = (tmpvar_41 * AmbientLightColor);
  highp vec4 v_45;
  v_45.x = Light0[0].y;
  v_45.y = Light0[1].y;
  v_45.z = Light0[2].y;
  v_45.w = Light0[3].y;
  highp vec4 v_46;
  v_46.x = Light0[0].x;
  v_46.y = Light0[1].x;
  v_46.z = Light0[2].x;
  v_46.w = Light0[3].x;
  highp vec3 tmpvar_47;
  tmpvar_47 = normalize(-(v_46.xyz));
  finalColor_43.xyz = (tmpvar_44 + ((
    (tmpvar_41 * max (dot (tmpvar_8, tmpvar_47), 0.0))
   + 
    (tmpvar_42 * pow (clamp (dot (
      (tmpvar_47 - (2.0 * (dot (tmpvar_8, tmpvar_47) * tmpvar_8)))
    , 
      -(normalize(tmpvar_9))
    ), 0.0, 1.0), tmpvar_42.w))
  ) * v_45)).xyz;
  finalColor_43.w = tmpvar_44.w;
  finalColor_3 = (((1.0 - GridParams.Lighting.x) * tmpvar_41) + (GridParams.Lighting.x * clamp (finalColor_43, 0.0, 1.0)));
  finalColor_3 = (finalColor_3 * GridParams.Params.y);
  highp vec4 tmpvar_48;
  tmpvar_48 = mix (finalColor_3, GridParams.ContourColorY, vec4((GridParams.ContourColorY.w * (1.0 - tmpvar_11.y))));
  finalColor_3.xyz = tmpvar_48.xyz;
  finalColor_3.w = (tmpvar_48.w * (baseColor_2.w * Material.m_DiffuseColor.w));
  highp vec4 tmpvar_49;
  tmpvar_49.xyz = finalColor_3.xyz;
  tmpvar_49.w = GridParams.Params.y;
  bvec4 tmpvar_50;
  tmpvar_50 = bvec4(tmpvar_49);
  bool tmpvar_51;
  tmpvar_51 = any(tmpvar_50);
  highp float tmpvar_52;
  if (tmpvar_51) {
    tmpvar_52 = (finalColor_3.w - 0.00390625);
  } else {
    tmpvar_52 = -1.0;
  };
  if ((tmpvar_52 < 0.0)) {
    discard;
  };
  gl_FragData[0] = finalColor_3;
}

