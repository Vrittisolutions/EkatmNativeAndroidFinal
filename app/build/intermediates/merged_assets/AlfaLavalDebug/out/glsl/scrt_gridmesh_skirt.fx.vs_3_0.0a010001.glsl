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
uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 PackedFloatParams;
uniform highp vec4 ClipPlanes[6];
uniform sGridParams GridParams;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
varying highp vec4 xlv_COLOR4;
varying highp vec2 xlv_COLOR5;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.zw = vTexCoord0.zw;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec4 tmpvar_6;
  highp vec2 tmpvar_7;
  highp mat4 tmpvar_8;
  tmpvar_8 = (matWorldView * matProj);
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_9;
  _texCoord_9 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_9 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_9);
  lowp vec4 tmpvar_10;
  tmpvar_10 = texture2DLod (HeightMapTexture, _texCoord_9, 0.0);
  highp vec4 rgba_11;
  rgba_11 = tmpvar_10.zyxw;
  tmpvar_1.y = (((
    dot (rgba_11.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8))
   * PackedFloatParams.y) + PackedFloatParams.x) * vPosition.w);
  highp vec3 tmpvar_12;
  highp mat3 tmpvar_13;
  tmpvar_13[0] = matWorld[0].xyz;
  tmpvar_13[1] = matWorld[1].xyz;
  tmpvar_13[2] = matWorld[2].xyz;
  tmpvar_12 = (GridParams.SurfaceNormal.xyz * tmpvar_13);
  highp vec2 _texCoord_14;
  _texCoord_14 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_14 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_14);
  lowp vec4 tmpvar_15;
  tmpvar_15 = texture2DLod (HeightMapTexture, _texCoord_14, 0.0);
  highp vec4 rgba_16;
  rgba_16 = tmpvar_15.zyxw;
  highp float tmpvar_17;
  tmpvar_17 = GridParams.Params.w;
  highp float tmpvar_18;
  tmpvar_18 = mix (tmpvar_1.y, ((
    (dot (rgba_16.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x) * vPosition.w), tmpvar_17);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_17));
  tmpvar_5 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_4 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  highp vec4 tmpvar_19;
  tmpvar_19 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_20;
  tmpvar_20 = bvec4(ClipPlanes[0]);
  bool tmpvar_21;
  tmpvar_21 = any(tmpvar_20);
  highp float tmpvar_22;
  if (tmpvar_21) {
    highp vec4 tmpvar_23;
    tmpvar_23.w = 1.0;
    tmpvar_23.xyz = tmpvar_19.xyz;
    tmpvar_22 = dot (tmpvar_23, ClipPlanes[0]);
  } else {
    tmpvar_22 = 1.0;
  };
  tmpvar_6.x = tmpvar_22;
  bvec4 tmpvar_24;
  tmpvar_24 = bvec4(ClipPlanes[1]);
  bool tmpvar_25;
  tmpvar_25 = any(tmpvar_24);
  highp float tmpvar_26;
  if (tmpvar_25) {
    highp vec4 tmpvar_27;
    tmpvar_27.w = 1.0;
    tmpvar_27.xyz = tmpvar_19.xyz;
    tmpvar_26 = dot (tmpvar_27, ClipPlanes[1]);
  } else {
    tmpvar_26 = 1.0;
  };
  tmpvar_6.y = tmpvar_26;
  bvec4 tmpvar_28;
  tmpvar_28 = bvec4(ClipPlanes[2]);
  bool tmpvar_29;
  tmpvar_29 = any(tmpvar_28);
  highp float tmpvar_30;
  if (tmpvar_29) {
    highp vec4 tmpvar_31;
    tmpvar_31.w = 1.0;
    tmpvar_31.xyz = tmpvar_19.xyz;
    tmpvar_30 = dot (tmpvar_31, ClipPlanes[2]);
  } else {
    tmpvar_30 = 1.0;
  };
  tmpvar_6.z = tmpvar_30;
  bvec4 tmpvar_32;
  tmpvar_32 = bvec4(ClipPlanes[3]);
  bool tmpvar_33;
  tmpvar_33 = any(tmpvar_32);
  highp float tmpvar_34;
  if (tmpvar_33) {
    highp vec4 tmpvar_35;
    tmpvar_35.w = 1.0;
    tmpvar_35.xyz = tmpvar_19.xyz;
    tmpvar_34 = dot (tmpvar_35, ClipPlanes[3]);
  } else {
    tmpvar_34 = 1.0;
  };
  tmpvar_6.w = tmpvar_34;
  bvec4 tmpvar_36;
  tmpvar_36 = bvec4(ClipPlanes[4]);
  bool tmpvar_37;
  tmpvar_37 = any(tmpvar_36);
  highp float tmpvar_38;
  if (tmpvar_37) {
    highp vec4 tmpvar_39;
    tmpvar_39.w = 1.0;
    tmpvar_39.xyz = tmpvar_19.xyz;
    tmpvar_38 = dot (tmpvar_39, ClipPlanes[4]);
  } else {
    tmpvar_38 = 1.0;
  };
  tmpvar_7.x = tmpvar_38;
  bvec4 tmpvar_40;
  tmpvar_40 = bvec4(ClipPlanes[5]);
  bool tmpvar_41;
  tmpvar_41 = any(tmpvar_40);
  highp float tmpvar_42;
  if (tmpvar_41) {
    highp vec4 tmpvar_43;
    tmpvar_43.w = 1.0;
    tmpvar_43.xyz = tmpvar_19.xyz;
    tmpvar_42 = dot (tmpvar_43, ClipPlanes[5]);
  } else {
    tmpvar_42 = 1.0;
  };
  tmpvar_7.y = tmpvar_42;
  tmpvar_1.w = 1.0;
  tmpvar_3.xy = tmpvar_2.xy;
  tmpvar_3.zw = tmpvar_2.zw;
  gl_Position = (tmpvar_1 * tmpvar_8);
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = tmpvar_12;
  xlv_TEXCOORD6 = tmpvar_18;
  xlv_TEXCOORD7 = tmpvar_5;
  xlv_COLOR4 = tmpvar_6;
  xlv_COLOR5 = tmpvar_7;
}

