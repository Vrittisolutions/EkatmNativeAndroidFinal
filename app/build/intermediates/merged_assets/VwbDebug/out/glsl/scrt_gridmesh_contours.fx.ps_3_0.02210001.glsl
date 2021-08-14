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
  highp vec2 BaseTexCoord_13;
  BaseTexCoord_13.x = _screenUV_12.x;
  BaseTexCoord_13.y = -(_screenUV_12.y);
  BaseTexCoord_13 = ((BaseTexCoord_13 + 1.0) * 0.5);
  BaseTexCoord_13 = (BaseTexCoord_13 + screenSize.zw);
  BaseTexCoord_13.y = (1.0 - BaseTexCoord_13.y);
  lowp vec4 tmpvar_14;
  tmpvar_14 = texture2D (DepthTexture, BaseTexCoord_13);
  highp float tmpvar_15;
  highp vec4 rgba_16;
  rgba_16 = tmpvar_14;
  tmpvar_15 = dot (rgba_16.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8));
  if ((tmpvar_15 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_17;
  tmpvar_17.w = 1.0;
  tmpvar_17.xy = _screenUV_12;
  tmpvar_17.z = tmpvar_15;
  highp vec4 tmpvar_18;
  tmpvar_18 = (tmpvar_17 * matInvViewProj);
  highp vec2 tmpvar_19;
  tmpvar_19.y = 0.0;
  tmpvar_19.x = (screenSize.z * 2.0);
  highp vec2 _screenUV_20;
  _screenUV_20 = (_screenUV_12 + tmpvar_19);
  highp vec2 BaseTexCoord_21;
  BaseTexCoord_21.x = _screenUV_20.x;
  BaseTexCoord_21.y = -(_screenUV_20.y);
  BaseTexCoord_21 = ((BaseTexCoord_21 + 1.0) * 0.5);
  BaseTexCoord_21 = (BaseTexCoord_21 + screenSize.zw);
  BaseTexCoord_21.y = (1.0 - BaseTexCoord_21.y);
  lowp vec4 tmpvar_22;
  tmpvar_22 = texture2D (DepthTexture, BaseTexCoord_21);
  highp float tmpvar_23;
  highp vec4 rgba_24;
  rgba_24 = tmpvar_22;
  tmpvar_23 = dot (rgba_24.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8));
  if ((tmpvar_23 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_25;
  tmpvar_25.w = 1.0;
  tmpvar_25.xy = _screenUV_20;
  tmpvar_25.z = tmpvar_23;
  highp vec4 tmpvar_26;
  tmpvar_26 = (tmpvar_25 * matInvViewProj);
  highp vec2 tmpvar_27;
  tmpvar_27.x = 0.0;
  tmpvar_27.y = (screenSize.w * 2.0);
  highp vec2 _screenUV_28;
  _screenUV_28 = (_screenUV_12 + tmpvar_27);
  highp vec2 BaseTexCoord_29;
  BaseTexCoord_29.x = _screenUV_28.x;
  BaseTexCoord_29.y = -(_screenUV_28.y);
  BaseTexCoord_29 = ((BaseTexCoord_29 + 1.0) * 0.5);
  BaseTexCoord_29 = (BaseTexCoord_29 + screenSize.zw);
  BaseTexCoord_29.y = (1.0 - BaseTexCoord_29.y);
  lowp vec4 tmpvar_30;
  tmpvar_30 = texture2D (DepthTexture, BaseTexCoord_29);
  highp float tmpvar_31;
  highp vec4 rgba_32;
  rgba_32 = tmpvar_30;
  tmpvar_31 = dot (rgba_32.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8));
  if ((tmpvar_31 == 0.0)) {
    discard;
  };
  highp vec4 tmpvar_33;
  tmpvar_33.w = 1.0;
  tmpvar_33.xy = _screenUV_28;
  tmpvar_33.z = tmpvar_31;
  highp vec4 tmpvar_34;
  tmpvar_34 = (tmpvar_33 * matInvViewProj);
  highp vec3 tmpvar_35;
  tmpvar_35 = (((tmpvar_18 / tmpvar_18.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz);
  highp vec3 tmpvar_36;
  tmpvar_36 = (abs((
    (((tmpvar_26 / tmpvar_26.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_35)) + abs((
    (((tmpvar_34 / tmpvar_34.w).xyz + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_35)));
  highp vec3 tmpvar_37;
  tmpvar_37 = (GridParams.ContourThickness.xyz * 0.5);
  highp vec3 tmpvar_38;
  tmpvar_38 = clamp ((abs(
    fract(tmpvar_35)
  ) / (tmpvar_36 * tmpvar_37)), 0.0, 1.0);
  highp vec3 tmpvar_39;
  tmpvar_39 = clamp (((1.0 - 
    abs(fract(tmpvar_35))
  ) / (tmpvar_36 * tmpvar_37)), 0.0, 1.0);
  tmpvar_11 = ((tmpvar_39 * (tmpvar_39 * 
    (3.0 - (2.0 * tmpvar_39))
  )) * (tmpvar_38 * (tmpvar_38 * 
    (3.0 - (2.0 * tmpvar_38))
  )));
  lowp vec4 tmpvar_40;
  tmpvar_40 = impl_low_texture2DLodEXT (CellInfoTexture, xlv_TEXCOORD0.zw, 0.0);
  highp vec4 tmpvar_41;
  tmpvar_41 = tmpvar_40;
  bvec4 tmpvar_42;
  tmpvar_42 = bvec4(tmpvar_41);
  bool tmpvar_43;
  tmpvar_43 = any(tmpvar_42);
  highp vec4 tmpvar_44;
  if (tmpvar_43) {
    tmpvar_44 = tmpvar_41;
  } else {
    tmpvar_44 = baseColor_2;
  };
  highp vec4 tmpvar_45;
  tmpvar_45.xyz = Material.m_SpecularColor.xyz;
  tmpvar_45.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_46;
  highp vec4 tmpvar_47;
  tmpvar_47 = (tmpvar_44 * AmbientLightColor);
  highp vec4 v_48;
  v_48.x = Light0[0].y;
  v_48.y = Light0[1].y;
  v_48.z = Light0[2].y;
  v_48.w = Light0[3].y;
  highp vec4 v_49;
  v_49.x = Light0[0].x;
  v_49.y = Light0[1].x;
  v_49.z = Light0[2].x;
  v_49.w = Light0[3].x;
  highp vec3 tmpvar_50;
  tmpvar_50 = normalize(-(v_49.xyz));
  finalColor_46.xyz = (tmpvar_47 + ((
    (tmpvar_44 * max (dot (tmpvar_8, tmpvar_50), 0.0))
   + 
    (tmpvar_45 * pow (clamp (dot (
      (tmpvar_50 - (2.0 * (dot (tmpvar_8, tmpvar_50) * tmpvar_8)))
    , 
      -(normalize(tmpvar_9))
    ), 0.0, 1.0), tmpvar_45.w))
  ) * v_48)).xyz;
  finalColor_46.w = tmpvar_47.w;
  finalColor_3 = (((1.0 - GridParams.Lighting.x) * tmpvar_44) + (GridParams.Lighting.x * clamp (finalColor_46, 0.0, 1.0)));
  finalColor_3 = (finalColor_3 * GridParams.Params.y);
  highp vec4 tmpvar_51;
  tmpvar_51 = mix (finalColor_3, GridParams.ContourColorY, vec4((GridParams.ContourColorY.w * (1.0 - tmpvar_11.y))));
  finalColor_3.xyz = tmpvar_51.xyz;
  finalColor_3.w = (tmpvar_51.w * (baseColor_2.w * Material.m_DiffuseColor.w));
  highp vec4 tmpvar_52;
  tmpvar_52.xyz = finalColor_3.xyz;
  tmpvar_52.w = GridParams.Params.y;
  bvec4 tmpvar_53;
  tmpvar_53 = bvec4(tmpvar_52);
  bool tmpvar_54;
  tmpvar_54 = any(tmpvar_53);
  highp float tmpvar_55;
  if (tmpvar_54) {
    tmpvar_55 = (finalColor_3.w - 0.00390625);
  } else {
    tmpvar_55 = -1.0;
  };
  if ((tmpvar_55 < 0.0)) {
    discard;
  };
  gl_FragData[0] = finalColor_3;
}

