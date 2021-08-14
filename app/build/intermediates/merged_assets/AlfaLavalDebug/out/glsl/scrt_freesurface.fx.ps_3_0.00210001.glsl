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
uniform highp vec3 CameraPosition;
uniform highp vec4 screenSize;
uniform highp vec4 DataPositionOffset;
uniform highp vec4 DisplacementAxesConstraints;
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
  highp float fAzimuthalWeight_6;
  highp float fSampleCoord_7;
  highp vec3 tmpvar_8;
  tmpvar_8 = (xlv_TEXCOORD1.xyz - DataPositionOffset.xyz);
  highp vec3 tmpvar_9;
  tmpvar_9.x = GradientMinMax.XMinMaxFactor.x;
  tmpvar_9.y = GradientMinMax.YMinMaxFactor.x;
  tmpvar_9.z = GradientMinMax.ZMinMaxFactor.x;
  highp vec3 tmpvar_10;
  tmpvar_10.x = GradientMinMax.XMinMaxFactor.y;
  tmpvar_10.y = GradientMinMax.YMinMaxFactor.y;
  tmpvar_10.z = GradientMinMax.ZMinMaxFactor.y;
  highp vec3 tmpvar_11;
  tmpvar_11.x = GradientMinMax.XMinMaxFactor.z;
  tmpvar_11.y = GradientMinMax.YMinMaxFactor.z;
  tmpvar_11.z = GradientMinMax.ZMinMaxFactor.z;
  highp float tmpvar_12;
  tmpvar_12 = GradientMinMax.YMinMaxFactor.w;
  highp float tmpvar_13;
  tmpvar_13 = GradientMinMax.ZMinMaxFactor.w;
  highp vec3 tmpvar_14;
  tmpvar_14 = clamp (((xlv_TEXCOORD1.xyz - tmpvar_9) / (tmpvar_10 - tmpvar_9)), 0.0, 1.0);
  highp vec3 tmpvar_15;
  tmpvar_15 = (tmpvar_8 * clamp (ceil(DisplacementAxesConstraints.xyz), 0.0, 1.0));
  highp float tmpvar_16;
  tmpvar_16 = dot (tmpvar_9, tmpvar_9);
  highp float tmpvar_17;
  tmpvar_17 = clamp (((
    dot (tmpvar_15, tmpvar_15)
   - tmpvar_16) / (
    dot (tmpvar_10, tmpvar_10)
   - tmpvar_16)), 0.0, 1.0);
  fSampleCoord_7 = (dot ((tmpvar_14 * 
    (tmpvar_14 * (3.0 - (2.0 * tmpvar_14)))
  ), tmpvar_11) + ((tmpvar_17 * 
    (tmpvar_17 * (3.0 - (2.0 * tmpvar_17)))
  ) * GradientMinMax.XMinMaxFactor.w));
  highp vec3 tmpvar_18;
  tmpvar_18 = normalize(tmpvar_8);
  highp vec3 tmpvar_19;
  tmpvar_19.y = 0.0;
  tmpvar_19.xz = tmpvar_18.xz;
  fAzimuthalWeight_6 = ((dot (
    normalize(tmpvar_19)
  , vec3(-1.0, 0.0, 0.0)) + 1.0) / 4.0);
  if ((tmpvar_18.z < 0.0)) {
    fAzimuthalWeight_6 = (1.0 - fAzimuthalWeight_6);
  };
  fSampleCoord_7 = (fSampleCoord_7 + (fAzimuthalWeight_6 * tmpvar_12));
  fSampleCoord_7 = (fSampleCoord_7 + ((
    (tmpvar_18.y + 1.0)
   / 2.0) * tmpvar_13));
  highp float tmpvar_20;
  tmpvar_20 = clamp (fSampleCoord_7, 0.0, 1.0);
  fSampleCoord_7 = tmpvar_20;
  highp vec2 tmpvar_21;
  tmpvar_21.y = 0.0;
  tmpvar_21.x = tmpvar_20;
  highp vec2 tmpvar_22;
  tmpvar_22 = mix (xlv_TEXCOORD0.xy, tmpvar_21, GridParams.Params.xx);
  highp vec3 tmpvar_23;
  tmpvar_23 = normalize(xlv_TEXCOORD2);
  highp vec3 tmpvar_24;
  tmpvar_24 = normalize((CameraPosition - xlv_TEXCOORD1.xyz));
  lowp vec4 tmpvar_25;
  tmpvar_25 = texture2D (DiffuseTexture, tmpvar_22);
  diffuseTextureColor_1 = tmpvar_25;
  baseColor_2 = (Material.m_DiffuseColor * diffuseTextureColor_1);
  highp float tmpvar_26;
  tmpvar_26 = max (xlv_TEXCOORD6, 1e-6);
  highp vec3 tmpvar_27;
  highp vec2 _screenUV_28;
  _screenUV_28 = (xlv_TEXCOORD5.xy / xlv_TEXCOORD5.w);
  highp vec4 fDepthPixel_29;
  highp vec2 BaseTexCoord_30;
  BaseTexCoord_30.x = _screenUV_28.x;
  BaseTexCoord_30.y = -(_screenUV_28.y);
  BaseTexCoord_30 = ((BaseTexCoord_30 + 1.0) * 0.5);
  BaseTexCoord_30 = (BaseTexCoord_30 + screenSize.zw);
  BaseTexCoord_30.y = (1.0 - BaseTexCoord_30.y);
  lowp vec4 tmpvar_31;
  tmpvar_31 = texture2D (DepthTexture, BaseTexCoord_30).xxxx;
  fDepthPixel_29 = tmpvar_31;
  highp vec2 result_32;
  highp float tmpvar_33;
  tmpvar_33 = fract(fDepthPixel_29.x);
  result_32.x = (float(int(
    (fDepthPixel_29.x - tmpvar_33)
  )) / 4096.0);
  result_32.y = tmpvar_33;
  highp vec3 tmpvar_34;
  tmpvar_34.x = result_32.x;
  tmpvar_34.y = tmpvar_26;
  tmpvar_34.z = result_32.y;
  highp vec2 tmpvar_35;
  tmpvar_35.y = 0.0;
  tmpvar_35.x = (screenSize.z * 2.0);
  highp vec2 _screenUV_36;
  _screenUV_36 = (_screenUV_28 + tmpvar_35);
  highp vec4 fDepthPixel_37;
  highp vec2 BaseTexCoord_38;
  BaseTexCoord_38.x = _screenUV_36.x;
  BaseTexCoord_38.y = -(_screenUV_36.y);
  BaseTexCoord_38 = ((BaseTexCoord_38 + 1.0) * 0.5);
  BaseTexCoord_38 = (BaseTexCoord_38 + screenSize.zw);
  BaseTexCoord_38.y = (1.0 - BaseTexCoord_38.y);
  lowp vec4 tmpvar_39;
  tmpvar_39 = texture2D (DepthTexture, BaseTexCoord_38).xxxx;
  fDepthPixel_37 = tmpvar_39;
  highp vec2 result_40;
  highp float tmpvar_41;
  tmpvar_41 = fract(fDepthPixel_37.x);
  result_40.x = (float(int(
    (fDepthPixel_37.x - tmpvar_41)
  )) / 4096.0);
  result_40.y = tmpvar_41;
  highp vec3 tmpvar_42;
  tmpvar_42.x = result_40.x;
  tmpvar_42.y = tmpvar_26;
  tmpvar_42.z = result_40.y;
  highp vec2 tmpvar_43;
  tmpvar_43.x = 0.0;
  tmpvar_43.y = (screenSize.w * 2.0);
  highp vec2 _screenUV_44;
  _screenUV_44 = (_screenUV_28 + tmpvar_43);
  highp vec4 fDepthPixel_45;
  highp vec2 BaseTexCoord_46;
  BaseTexCoord_46.x = _screenUV_44.x;
  BaseTexCoord_46.y = -(_screenUV_44.y);
  BaseTexCoord_46 = ((BaseTexCoord_46 + 1.0) * 0.5);
  BaseTexCoord_46 = (BaseTexCoord_46 + screenSize.zw);
  BaseTexCoord_46.y = (1.0 - BaseTexCoord_46.y);
  lowp vec4 tmpvar_47;
  tmpvar_47 = texture2D (DepthTexture, BaseTexCoord_46).xxxx;
  fDepthPixel_45 = tmpvar_47;
  highp vec2 result_48;
  highp float tmpvar_49;
  tmpvar_49 = fract(fDepthPixel_45.x);
  result_48.x = (float(int(
    (fDepthPixel_45.x - tmpvar_49)
  )) / 4096.0);
  result_48.y = tmpvar_49;
  highp vec3 tmpvar_50;
  tmpvar_50.x = result_48.x;
  tmpvar_50.y = tmpvar_26;
  tmpvar_50.z = result_48.y;
  highp vec3 tmpvar_51;
  tmpvar_51 = ((tmpvar_34 + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz);
  highp vec3 tmpvar_52;
  tmpvar_52 = (abs((
    ((tmpvar_42 + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_51)) + abs((
    ((tmpvar_50 + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz)
   - tmpvar_51)));
  highp vec3 tmpvar_53;
  tmpvar_53 = (GridParams.ContourThickness.xyz * 0.5);
  highp vec3 tmpvar_54;
  tmpvar_54 = clamp ((abs(
    fract(tmpvar_51)
  ) / (tmpvar_52 * tmpvar_53)), 0.0, 1.0);
  highp vec3 tmpvar_55;
  tmpvar_55 = clamp (((1.0 - 
    abs(fract(tmpvar_51))
  ) / (tmpvar_52 * tmpvar_53)), 0.0, 1.0);
  tmpvar_27 = ((tmpvar_55 * (tmpvar_55 * 
    (3.0 - (2.0 * tmpvar_55))
  )) * (tmpvar_54 * (tmpvar_54 * 
    (3.0 - (2.0 * tmpvar_54))
  )));
  finalColor_3 = (baseColor_2 * GridParams.Params.y);
  lowp vec4 tmpvar_56;
  tmpvar_56 = impl_low_texture2DLodEXT (CellInfoTexture, xlv_TEXCOORD0.xy, 0.0);
  highp vec4 tmpvar_57;
  tmpvar_57 = tmpvar_56;
  bvec4 tmpvar_58;
  tmpvar_58 = bvec4(tmpvar_57);
  bool tmpvar_59;
  tmpvar_59 = any(tmpvar_58);
  highp vec4 tmpvar_60;
  if (tmpvar_59) {
    tmpvar_60 = tmpvar_57;
  } else {
    tmpvar_60 = finalColor_3;
  };
  highp vec4 tmpvar_61;
  tmpvar_61.xyz = Material.m_SpecularColor.xyz;
  tmpvar_61.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_62;
  highp vec4 tmpvar_63;
  tmpvar_63 = (tmpvar_60 * AmbientLightColor);
  highp vec4 v_64;
  v_64.x = Light0[0].y;
  v_64.y = Light0[1].y;
  v_64.z = Light0[2].y;
  v_64.w = Light0[3].y;
  highp vec4 v_65;
  v_65.x = Light0[0].x;
  v_65.y = Light0[1].x;
  v_65.z = Light0[2].x;
  v_65.w = Light0[3].x;
  highp vec3 tmpvar_66;
  tmpvar_66 = normalize(-(v_65.xyz));
  finalColor_62.xyz = (tmpvar_63 + ((
    (tmpvar_60 * max (dot (tmpvar_23, tmpvar_66), 0.0))
   + 
    (tmpvar_61 * pow (clamp (dot (
      (tmpvar_66 - (2.0 * (dot (tmpvar_23, tmpvar_66) * tmpvar_23)))
    , 
      -(normalize(tmpvar_24))
    ), 0.0, 1.0), tmpvar_61.w))
  ) * v_64)).xyz;
  finalColor_62.w = tmpvar_63.w;
  finalColor_3 = (((1.0 - GridParams.Lighting.x) * tmpvar_60) + (GridParams.Lighting.x * clamp (finalColor_62, 0.0, 1.0)));
  highp vec4 tmpvar_67;
  tmpvar_67 = mix (mix (finalColor_3, GridParams.ContourColorX, vec4((GridParams.ContourColorX.w * 
    (1.0 - tmpvar_27.x)
  ))), GridParams.ContourColorZ, vec4((GridParams.ContourColorZ.w * (1.0 - tmpvar_27.z))));
  finalColor_3.xyz = tmpvar_67.xyz;
  finalColor_3.w = (tmpvar_67.w * (baseColor_2.w * Material.m_DiffuseColor.w));
  highp vec4 tmpvar_68;
  tmpvar_68.xyz = finalColor_3.xyz;
  tmpvar_68.w = GridParams.Params.y;
  bvec4 tmpvar_69;
  tmpvar_69 = bvec4(tmpvar_68);
  bool tmpvar_70;
  tmpvar_70 = any(tmpvar_69);
  highp float tmpvar_71;
  if (tmpvar_70) {
    tmpvar_71 = (finalColor_3.w - 0.00390625);
  } else {
    tmpvar_71 = -1.0;
  };
  if ((tmpvar_71 < 0.0)) {
    discard;
  };
  gl_FragData[0] = finalColor_3;
}

